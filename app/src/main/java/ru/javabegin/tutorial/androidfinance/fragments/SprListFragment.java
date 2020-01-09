package ru.javabegin.tutorial.androidfinance.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import ru.javabegin.tutorial.androidfinance.R;
import ru.javabegin.tutorial.androidfinance.core.database.Initializer;
import ru.javabegin.tutorial.androidfinance.core.interfaces.TreeNode;

public class SprListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener clickListener;

    private TreeNodeAdapter adapter;
    private List<? extends TreeNode> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.spr_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).margin(90, 0).build());
            recyclerView.setAdapter(adapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            clickListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }

        list = Initializer.getSourceSync().getAll();
        adapter = new TreeNodeAdapter(getContext(), list, clickListener);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        clickListener = null;
    }

    public void updateList(List<? extends TreeNode> list) {
        adapter.updateList(list, TreeNodeAdapter.animatorParents);
    }

    public void updateNode(TreeNode node) {
        adapter.updateNode(node);
    }

    public void addNode(TreeNode node) {
        adapter.addNode(node);
    }

    public void addChild(TreeNode node) {
        adapter.addChild(node);
    }

    public interface OnListFragmentInteractionListener {
        void onItemClicked(TreeNode node);
        void onPopupMenuClicked(TreeNode node);
    }
}
