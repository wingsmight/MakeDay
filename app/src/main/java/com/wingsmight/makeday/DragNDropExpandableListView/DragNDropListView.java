/*
 * Copyright (C) 2010 Eric Harlow
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wingsmight.makeday.DragNDropExpandableListView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wingsmight.makeday.R;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.CompoundButtonCompat;

public class DragNDropListView extends ExpandableListView
{

	boolean mDragMode;

	int mStartPosition;
	int mEndPosition;
	int mDragPointOffset;//Used to adjust drag view location
	
	ImageView mDragView;
	GestureDetector mGestureDetector;
	
	DropListener mDropListener;
	RemoveListener mRemoveListener;
	DragListener mDragListener;
	
	public DragNDropListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setDropListener(DropListener l) {
		mDropListener = l;
	}

	public void setRemoveListener(RemoveListener l) {
		mRemoveListener = l;
	}
	
	public void setDragListener(DragListener l) {
		mDragListener = l;
	}

	int prevPoint = -1;
	int prevCorrectPoint = -10;
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		final int x = (int) ev.getX();
		final int y = (int) ev.getY();	
		
		if (action == MotionEvent.ACTION_DOWN && x < this.getWidth()/4) {
			mDragMode = true;
		}

		if (!mDragMode) 
			return super.onTouchEvent(ev);

        //Log.i("msg", Integer.toString(y));
		switch (action) {
			case MotionEvent.ACTION_DOWN://finger down
            {
                prevPoint = -1;
                prevCorrectPoint = -10;

                mStartPosition = pointToPosition(x,y);

                if (mStartPosition != INVALID_POSITION)
                {
                    int mItemPosition = mStartPosition - getFirstVisiblePosition();
                    mDragPointOffset = y - getChildAt(mItemPosition).getTop();
                    mDragPointOffset -= ((int)ev.getRawY()) - y;
                    startDrag(mItemPosition,y);
                    drag(0,y);// replace 0 with x if desired
                }
                break;
            }
			case MotionEvent.ACTION_MOVE://finger on
            {
                drag(0,y);// replace 0 with x if desired

                if(pointToPosition(x,y) != prevCorrectPoint && pointToPosition(x,y) != -1)
                {
                    prevCorrectPoint = pointToPosition(x,y);
                }
                if(pointToPosition(x,y) != prevPoint)
                {
                    prevPoint = pointToPosition(x,y);

                    int to = pointToPosition(x,y);
                    int from = mStartPosition;

                    if(prevPoint == -1)
                    {
                        if(prevCorrectPoint == getCount() - 1)
                        {
                            to = getCount() - 1;
                        }
                        else
                        {
                            to = from;
                        }
                    }

                    if(to == -1)
                    {
                        if(y <= 0)
                        {
                            to = 0;
                        }
                        else
                        {
                            to = getCount() - 1;
                        }
                    }

                    if(isDragChild)
                    {
                        mDropListener.onSwapChild(from, to, this, prevPoint);
                    }
                    else
                    {
                        mDropListener.onSwap(from, to, this, prevPoint);
                    }
                }

                break;
            }
			case MotionEvent.ACTION_CANCEL:
            {
                Log.i("msg", "MotionEvent.ACTION_CANCEL");
            }
			case MotionEvent.ACTION_UP://finger up
            {

            }
			default:
            {
                mDragMode = false;
                mEndPosition = pointToPosition(x,y);
                Log.i("msg", Integer.toString(mEndPosition));
                if(prevPoint == -1)
                {
                    if(prevCorrectPoint == getCount() - 1)
                    {
                        mEndPosition = getCount() - 1;
                    }
                    else
                    {
                        mEndPosition = mStartPosition;
                    }
                }

                if(mEndPosition == -1)
                {
                    if(y <= 0)
                    {
                        mEndPosition = 0;
                    }
                    else
                    {
                        mEndPosition = getCount() - 1;
                    }
                }

                Log.i("msg", Integer.toString(mStartPosition));
                Log.i("msg", Integer.toString(mEndPosition));

                stopDrag(mStartPosition - getFirstVisiblePosition());

                if (mDropListener != null && mStartPosition != INVALID_POSITION && mEndPosition != INVALID_POSITION)
                {
                    if(isDragChild)
                    {
                        mDropListener.onDropChild(mStartPosition, mEndPosition);//TODO
                    }
                    else
                    {
                        mDropListener.onDrop(mStartPosition, mEndPosition);//TODO
                    }
                }

                break;
            }
		}
		return true;
	}
	
	// move the drag view
	private void drag(int x, int y) {
		if (mDragView != null) {
			WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) mDragView.getLayoutParams();
			layoutParams.x = x;
			layoutParams.y = y - mDragPointOffset;
			WindowManager mWindowManager = (WindowManager) getContext()
					.getSystemService(Context.WINDOW_SERVICE);
			mWindowManager.updateViewLayout(mDragView, layoutParams);

			if (mDragListener != null)
            {
                mDragListener.onDrag(x, y, this);// change null to "this" when ready to use
            }
		}
	}

	boolean isDragChild = false;
	// enable the drag view for dragging
	private void startDrag(int itemIndex, int y)
    {
		stopDrag(itemIndex);
        mDropListener.onStartDrag(itemIndex,this);

		View item = getChildAt(itemIndex);
		if (item == null)
        {
            return;
        }

        if(item.getId() == R.id.rowGenericSkillLayout)
        {
            isDragChild = false;

            TextView genericSkillName = item.findViewById(R.id.genericSkillName);
            genericSkillName.setTextColor(getContext().getResources().getColor(android.R.color.tab_indicator_text));
        }
        else if(item.getId() == R.id.rowSkillLayout)
        {
            isDragChild = true;

            TextView skillName = item.findViewById(R.id.skillName);
            skillName.setTextColor(getContext().getResources().getColor(android.R.color.tab_indicator_text));
        }


		item.setDrawingCacheEnabled(true);

		if (mDragListener != null)
        {
            mDragListener.onStartDrag(item);
        }
		
        // Create a copy of the drawing cache so that it does not get recycled
        // by the framework when the list tries to clean up memory
        Bitmap bitmap = Bitmap.createBitmap(item.getDrawingCache());
        
        WindowManager.LayoutParams mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.gravity = Gravity.TOP;
        mWindowParams.x = 0;
        mWindowParams.y = y - mDragPointOffset;

        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        mWindowParams.format = PixelFormat.TRANSLUCENT;
        mWindowParams.windowAnimations = 0;
        
        Context context = getContext();
        ImageView v = new ImageView(context);
        v.setImageBitmap(bitmap);
        //v.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_delete));
        //v.setBackgroundColor(getContext().getResources().getColor(R.color.transportSkillViewColor));

        WindowManager mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(v, mWindowParams);
        mDragView = v;
	}

	// destroy drag view
	private void stopDrag(int itemIndex) {
		if (mDragView != null) {
			if (mDragListener != null)
			{
                mDragListener.onStopDrag(getChildAt(itemIndex));
			}

            mDragView.setVisibility(GONE);
            WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
            wm.removeView(mDragView);
            mDragView.setImageDrawable(null);
            mDragView = null;
        }
	}



//	private GestureDetector createFlingDetector() {
//		return new GestureDetector(getContext(), new SimpleOnGestureListener() {
//            @Override
//            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//                    float velocityY) {
//                if (mDragView != null) {
//                	int deltaX = (int)Math.abs(e1.getX()-e2.getX());
//                	int deltaY = (int)Math.abs(e1.getY() - e2.getY());
//
//                	if (deltaX > mDragView.getWidth()/2 && deltaY < mDragView.getHeight()) {
//                		mRemoveListener.onRemove(mStartPosition);
//                	}
//
//                	stopDrag(mStartPosition - getFirstVisiblePosition());
//
//                    return true;
//                }
//                return false;
//            }
//        });
//	}
}
