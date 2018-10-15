package ch.epfl.swissteam.services;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.helper.ItemTouchHelper.Callback;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;
import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;

/**
 * MyPostsFragment, a fragment that display to the currently logged in user
 * his posts. Depending on his interaction with his posts, sends him to
 * {@link MyPostEdit}.
 */
public class MyPostsFragment extends Fragment {

    private String clientUniqueID_;
    private RecyclerView.Adapter mAdapter_;
    private List<Post> mPosts_ = new ArrayList<>();

    public MyPostsFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new {@link MyPostsFragment}.
     *
     * @return new instance of <code>MyPostsFragment</code>
     */
    public static MyPostsFragment newInstance() {
        MyPostsFragment fragment = new MyPostsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_my_posts);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View frag = inflater.inflate(R.layout.fragment_my_posts, container, false);

        clientUniqueID_ = GoogleSignInSingleton.get().getClientUniqueID();
        loadAndShowPostsFromUser();

        //setup recyclerview for posts
        RecyclerView mRecyclerView_ = frag.findViewById(R.id.recyclerview_mypostsfragment);

        if (mRecyclerView_ != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
            mRecyclerView_.setLayoutManager(layoutManager);

            mAdapter_ = new PostAdapter(mPosts_);
            mRecyclerView_.setAdapter(mAdapter_);
            SwipeController swipeController = new SwipeController();
            ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
            itemTouchhelper.attachToRecyclerView(mRecyclerView_);
        }

        return frag;
    }

    /**
     * Load the posts from a user and display them in the recycler view
     */
    private void loadAndShowPostsFromUser() {
        DBUtility.get().getUsersPosts(clientUniqueID_, (posts) -> {
            mPosts_.clear();
            mPosts_.addAll(posts);
            mAdapter_.notifyDataSetChanged();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAndShowPostsFromUser();
    }

    private static class SwipeController extends Callback {
        enum ButtonsState {
            GONE,
            RIGHT_VISIBLE
        }
        private boolean swipeBack = false;
        private ButtonsState buttonShowedState = ButtonsState.GONE;
        private static final float buttonWidth = 300;
        private RectF buttonInstance = null;

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(0, LEFT);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        //TODO
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public int convertToAbsoluteDirection(int flags, int layoutDirection) {
            if (swipeBack) {
                swipeBack = false;
                return 0;
            }
            return super.convertToAbsoluteDirection(flags, layoutDirection);
        }

        @Override
        public void onChildDraw(Canvas c,
                                RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder,
                                float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {

            if (actionState == ACTION_STATE_SWIPE) {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            drawButtons(c, viewHolder);
        }

        //TODO Comment
        private void setTouchListener(Canvas c,
                                      RecyclerView recyclerView,
                                      RecyclerView.ViewHolder viewHolder,
                                      float dX, float dY,
                                      int actionState, boolean isCurrentlyActive) {

            recyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                    if (swipeBack) {
                        if (dX < -buttonWidth) buttonShowedState = ButtonsState.RIGHT_VISIBLE;

                        if (buttonShowedState != ButtonsState.GONE) {
                            setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                            setItemsClickable(recyclerView, false);
                        }
                    }
                    return false;
                }
            });
        }

        //TODO comment
        private void setTouchDownListener(final Canvas c,
                                          final RecyclerView recyclerView,
                                          final RecyclerView.ViewHolder viewHolder,
                                          final float dX, final float dY,
                                          final int actionState, final boolean isCurrentlyActive) {
            recyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }
                    return false;
                }
            });
        }

        //TODO Comment
        private void setTouchUpListener(final Canvas c,
                                        final RecyclerView recyclerView,
                                        final RecyclerView.ViewHolder viewHolder,
                                        final float dX, final float dY,
                                        final int actionState, final boolean isCurrentlyActive) {
            recyclerView.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeController.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                    recyclerView.setOnTouchListener((v1, event1) -> false);
                    setItemsClickable(recyclerView, true);
                    swipeBack = false;
                    buttonShowedState = ButtonsState.GONE;
                }

                return false;
            });
        }

        //TODO Comment
        private void setItemsClickable(RecyclerView recyclerView,
                                       boolean isClickable) {
            for (int i = 0; i < recyclerView.getChildCount(); ++i) {
                recyclerView.getChildAt(i).setClickable(isClickable);
            }
        }

        //TODO change and comment
        private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
            float buttonWidthWithoutPadding = buttonWidth - 20;
            float corners = 16;

            View itemView = viewHolder.itemView;
            Paint p = new Paint();

            RectF leftButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom());
            p.setColor(Color.BLUE);
            c.drawRoundRect(leftButton, corners, corners, p);
            //drawText("EDIT", c, leftButton, p);

            RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            p.setColor(Color.RED);
            c.drawRoundRect(rightButton, corners, corners, p);
            //drawText("DELETE", c, rightButton, p);

            buttonInstance = null;
            if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
                buttonInstance = leftButton;
            }
        }

    }

}