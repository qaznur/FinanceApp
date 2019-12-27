package ru.javabegin.tutorial.androidfinance.fragments;

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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.sql.SQLException;
import java.util.List;

import ru.javabegin.tutorial.androidfinance.R;
import ru.javabegin.tutorial.androidfinance.activities.EditActivity;
import ru.javabegin.tutorial.androidfinance.core.database.Initializer;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Source;
import ru.javabegin.tutorial.androidfinance.core.interfaces.TreeNode;
import ru.javabegin.tutorial.androidfinance.fragments.SprFragment.OnListFragmentInteractionListener;

public class TreeNodeAdapter<T extends TreeNode> extends RecyclerView.Adapter<TreeNodeAdapter.ViewHolder> {

    private static final String TAG = TreeNodeAdapter.class.getName();

    public static final String SPR_NAME = "sprName";

    private Context context;
    private List<T> list;
    private final OnListFragmentInteractionListener clickListener;

    public TreeNodeAdapter(Context context, List<T> items, OnListFragmentInteractionListener listener) {
        list = items;
        clickListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spr_node, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TreeNodeAdapter.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder");

        final TreeNode node = list.get(position);

        holder.tvSprName.setText(node.getName());
        String childCount;
        if (!node.getChildren().isEmpty()) {
            childCount = String.valueOf(node.getChildren().size());
        } else {
            childCount = "";
        }
        holder.tvChildCount.setText(childCount);
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != clickListener) {
                    clickListener.onItemClicked(node);
                }

                if (node.hasChilds()) {
                    updateList((List<T>) node.getChildren());
                }
            }
        });

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

                        } else if (id == R.id.item_edit) {
                            Intent intent = new Intent(context, EditActivity.class);
                            intent.putExtra(SPR_NAME, node.getName());
                            context.startActivity(intent);

                        } else if (id == R.id.item_delete) {
                            new AlertDialog.Builder(context)
                                    .setTitle(R.string.confirm)
                                    .setMessage(R.string.confirm_delete)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            list.remove(node);
                                            notifyItemRemoved(position);

                                            Snackbar.make(holder.buttonPopup, R.string.deleted, Snackbar.LENGTH_SHORT)
                                                    .setAction(R.string.undo, new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            if (node.hasParent()) {
                                                                node.getParent().add(node);
                                                            } else {
                                                                list.add(position, (T) node);
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
                                            }).show();

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
        return list.size();
    }

    public void updateList(List<T> newList) {
        list = newList;
        notifyDataSetChanged();
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
