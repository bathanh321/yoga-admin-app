package com.example.yogaapp.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yogaapp.R;

public abstract class SwipeToActionCallback extends ItemTouchHelper.Callback {

    private final Context context;

    public SwipeToActionCallback(Context context) {
        this.context = context;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.LEFT) {
            onItemSwipedToDelete(viewHolder.getAdapterPosition());
        } else {
            onItemSwipedToEdit(viewHolder.getAdapterPosition());
        }
    }

    protected abstract void onItemSwipedToEdit(int position);
    protected abstract void onItemSwipedToDelete(int position);

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;

        if (dX > 0) {
            drawEditBackground(c, itemView, dX);
            drawEditIcon(c, itemView);
        } else if (dX < 0) {
            drawDeleteBackground(c, itemView, dX);
            drawDeleteIcon(c, itemView);
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void drawEditBackground(Canvas c, View itemView, float dX) {
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.amber_300));
        c.drawRect(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + dX, itemView.getBottom(), paint);
    }

    private void drawDeleteBackground(Canvas c, View itemView, float dX) {
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.rose_900));
        c.drawRect(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom(), paint);
    }

    private void drawEditIcon(Canvas c, View itemView) {
        Drawable icon = ContextCompat.getDrawable(context, R.drawable.ic_edit);
        if (icon != null) {
            int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconTop = itemView.getTop() + iconMargin;
            int iconBottom = iconTop + icon.getIntrinsicHeight();
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = iconLeft + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            icon.draw(c);
        }
    }

    private void drawDeleteIcon(Canvas c, View itemView) {
        Drawable icon = ContextCompat.getDrawable(context, R.drawable.ic_trash);
        if (icon != null) {
            int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconTop = itemView.getTop() + iconMargin;
            int iconBottom = iconTop + icon.getIntrinsicHeight();
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = iconLeft + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            icon.draw(c);
        }
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        // Cho phép vuốt trái và phải
        return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }
}
