package ru.javabegin.tutorial.androidfinance.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.sql.SQLException;
import java.util.List;

import jp.wasabeef.recyclerview.animators.BaseItemAnimator;
import ru.javabegin.tutorial.androidfinance.R;
import ru.javabegin.tutorial.androidfinance.activities.EditSourceActivity;
import ru.javabegin.tutorial.androidfinance.core.database.Initializer;
import ru.javabegin.tutorial.androidfinance.core.impls.DefaultSource;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Source;
import ru.javabegin.tutorial.androidfinance.core.interfaces.TreeNode;
import ru.javabegin.tutorial.androidfinance.fragments.SprListFragment.OnListFragmentInteractionListener;

public class TreeNodeAdapter<T extends TreeNode> extends RecyclerView.Adapter<TreeNodeAdapter.ViewHolder> {

    private static final String TAG = TreeNodeAdapter.class.getName();

    private Context context;
    private List<T> adapterList;
    private final OnListFragmentInteractionListener clickListener;

    private int selectedNodePosition;

    public static BaseItemAnimator animatorChilds; // анимация при открытии дочерних элементов (справа налево)
    public static BaseItemAnimator animatorParents; // анимация при открытии родительских элементов (слево направо)

    private Snackbar snackbar;
    private RecyclerView recyclerView;

    public TreeNodeAdapter(Context context, List<T> items, OnListFragmentInteractionListener listener) {
        adapterList = items;
        clickListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        snackbar = Snackbar.make(parent, R.string.deleted, Snackbar.LENGTH_LONG);
        recyclerView = (RecyclerView) parent;
        createAnimations();

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spr_node, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TreeNodeAdapter.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder");

        final TreeNode node = adapterList.get(position);

        holder.tvSprName.setText(node.getName());
        String childCount;
        if (node.hasChilds()) {
            childCount = String.valueOf(node.getChildren().size());
//            holder.layoutItem.setEnabled(true);
            holder.tvChildCount.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGray));
        } else {
            childCount = "";
//            holder.layoutItem.setEnabled(false);
            holder.tvChildCount.setBackground(null);
        }
        holder.tvChildCount.setText(childCount);
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != clickListener) {
                    clickListener.onItemClicked(node);
                }

                if (node.hasChilds()) {
                    updateList((List<T>) node.getChildren(), animatorChilds);
                } else {
                    selectedNodePosition = position;
                    runActivity(node, EditSourceActivity.REQUEST_NODE_EDIT);
                }
            }
        });

        initPopupMenu(holder, position, node);
    }

    private void initPopupMenu(final ViewHolder holder, final int position, final TreeNode node) {
        holder.buttonPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu dropDownPopupMenu = new PopupMenu(context, holder.buttonPopup);
                dropDownPopupMenu.getMenuInflater().inflate(R.menu.spr_popup_menu, dropDownPopupMenu.getMenu());
                MenuItem menuItem = dropDownPopupMenu.getMenu().findItem(R.id.item_delete);
                if (node.hasChilds()) {
                    menuItem.setEnabled(false);
                }
                dropDownPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();

                        if (id == R.id.item_add) {
                            DefaultSource source = new DefaultSource();
                            source.setOperationType(((Source) node).getOperationType());

                            clickListener.onPopupMenuClicked(node);
                            runActivity(source, EditSourceActivity.REQUEST_CHILD_NODE_ADD);
                        } else if (id == R.id.item_edit) {
                            selectedNodePosition = position;
                            runActivity(node, EditSourceActivity.REQUEST_NODE_EDIT);
                        } else if (id == R.id.item_delete) {
                            new AlertDialog.Builder(context)
                                    .setTitle(R.string.confirm)
                                    .setMessage(R.string.confirm_delete)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            adapterList.remove(node);
                                            notifyItemRemoved(position);

                                            snackbar.setAction(R.string.undo, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (node.hasParent()) {
                                                        node.getParent().add(node);
                                                    } else {
                                                        adapterList.add(position, (T) node);
                                                    }
                                                    notifyDataSetChanged();
                                                }
                                            }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                                @Override
                                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                                    if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT
                                                            || event == Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE) {
                                                        deleteNode((Source) node, position);
                                                    }
                                                }
                                            });
                                            snackbar.show();

                                        }
                                    }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                        }
                        return true;
                    }
                });
                dropDownPopupMenu.show();
            }
        });
    }

    private void createAnimations() {

        animatorParents = new BaseItemAnimator() {

            @Override
            protected void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
                ViewCompat.animate(holder.itemView)
                        .translationX(holder.itemView.getRootView().getWidth())
                        .setDuration(getRemoveDuration())
                        .setInterpolator(mInterpolator)
                        .setListener(new DefaultRemoveVpaListener(holder))
                        .setStartDelay(getRemoveDelay(holder))
                        .start();
            }

            @Override
            protected void preAnimateAddImpl(RecyclerView.ViewHolder holder) {
                ViewCompat.setTranslationX(holder.itemView, -holder.itemView.getRootView().getWidth());
            }

            @Override
            protected void animateAddImpl(final RecyclerView.ViewHolder holder) {
                ViewCompat.animate(holder.itemView)
                        .translationX(0)
                        .setDuration(getAddDuration())
                        .setInterpolator(mInterpolator)
                        .setListener(new DefaultAddVpaListener(holder))
                        .setStartDelay(getAddDelay(holder))
                        .start();
            }
        };


        animatorChilds = new BaseItemAnimator() {


            @Override
            protected void animateRemoveImpl(final RecyclerView.ViewHolder holder) {

                ViewCompat.animate(holder.itemView)
                        .translationX(-holder.itemView.getRootView().getWidth())
                        .setDuration(getRemoveDuration())
                        .setInterpolator(mInterpolator)
                        .setListener(new DefaultRemoveVpaListener(holder))
                        .setStartDelay(getRemoveDelay(holder))
                        .start();

            }

            @Override
            protected void preAnimateAddImpl(RecyclerView.ViewHolder holder) {
                ViewCompat.setTranslationX(holder.itemView, holder.itemView.getRootView().getWidth());
            }

            @Override
            protected void animateAddImpl(final RecyclerView.ViewHolder holder) {
                ViewCompat.animate(holder.itemView)
                        .translationX(0)
                        .setDuration(getAddDuration())
                        .setInterpolator(mInterpolator)
                        .setListener(new DefaultAddVpaListener(holder))
                        .setStartDelay(getAddDelay(holder))
                        .start();
            }
        };

    }

    private void hideSnackbar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

    private void runActivity(TreeNode node, int requestCode) {
        hideSnackbar();

        Intent intent = new Intent(context, EditSourceActivity.class);
        intent.putExtra(EditSourceActivity.NODE_OBJECT, node);
        ((Activity) context).startActivityForResult(intent, requestCode, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context).toBundle());
    }

    private void deleteNode(Source node, int position) {
        try {
            Initializer.getSourceSync().delete(node);
            notifyItemRemoved(position);
        } catch (SQLException e) {
            Log.d(TAG, e.getMessage());
            if (e.getMessage().contains("SQLiteConstraintException")) {
                Toast.makeText(context, R.string.has_operations, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public void updateList(List<T> newList, RecyclerView.ItemAnimator animator) {
        hideSnackbar();

        recyclerView.setItemAnimator(animator);
        int range = adapterList.size();
        notifyItemRangeRemoved(0, range);

        adapterList = newList;
        notifyItemRangeInserted(0, adapterList.size());
    }

    public void updateNode(TreeNode node) {
        try {
            Initializer.getSourceSync().update((Source) node);
            notifyItemChanged(selectedNodePosition);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addNode(TreeNode node) {
        try {
            Source source = (Source) node;
            Initializer.getSourceSync().add(source);
            notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addChild(TreeNode node) {
        try {
            Source source = (Source) node;
            Initializer.getSourceSync().add(source);
            adapterList = (List<T>) node.getParent().getChildren();
            clickListener.onItemClicked(node.getParent());
            notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView tvSprName;
        final TextView tvChildCount;
        final ViewGroup layoutItem;
        final ImageView buttonPopup;


        ViewHolder(View view) {
            super(view);
            tvSprName = view.findViewById(R.id.spr_name);
            tvChildCount = view.findViewById(R.id.child_count);
            layoutItem = view.findViewById(R.id.item_layout);
            buttonPopup = view.findViewById(R.id.popup_button);
        }
    }
}
