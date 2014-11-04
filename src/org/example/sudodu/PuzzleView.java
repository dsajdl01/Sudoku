package org.example.sudodu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.os.Bundle;
import android.os.Parcelable;

@SuppressLint("DrawAllocation")
public class PuzzleView extends View{
	private static final String TAG = "Sudoku";
	private final Game game;
	
	private static final String SELX = "selX";
	private static final String SELY = "selY";
	private static final String VIEW_STATE = "viewState";
	private static final int ID = 42;
	
	private float width;
	private float height;
	private int selX;
	private int selY;
	private final Rect selRect = new Rect();
	
	public PuzzleView(Context context){
		super(context);
		this.game = (Game) context;
		setFocusable(true);
		//setFocusableinTouchMode(true);
		setId(ID);
	}
	
	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable p = super.onSaveInstanceState();
		Log.d(TAG, "onSaveInstanceState");
		Bundle bundle = new Bundle();
		bundle.putInt(SELX, selX);
		bundle.putInt(SELY, selY);
		bundle.putParcelable(VIEW_STATE, p);
		return bundle;
	}
	@Override
		protected void onRestoreInstanceState(Parcelable state) {
		Log.d(TAG, "onRestoreInstanceState");
		Bundle bundle = (Bundle) state;
		select(bundle.getInt(SELX), bundle.getInt(SELY));
		super.onRestoreInstanceState(bundle.getParcelable(VIEW_STATE));
		return ;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh){
		width = w /9f;
		height = h/9f;
		getRect(selX, selY, selRect);
		Log.d(TAG, "onSizeChanged: width " + width + ", height " + height);
		super.onSizeChanged(w, h, oldw, oldh);
	}
	private void getRect(int x, int y, Rect rect){
		rect.set((int) (x*width), (int) (y*height), (int) (x*width + width), (int) (y*height + height));
	}
	
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas){
		// draw background
		Paint background = new Paint();
		background.setColor(getResources().getColor(R.color.puzzle_background));
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);
		//draw board, number hints, selection
		Paint dark = new Paint();
		dark.setColor(getResources().getColor(R.color.puzzle_dark));
		
		Paint hilite = new Paint();
		hilite.setColor(getResources().getColor(R.color.puzzle_hilite));
		
		Paint light = new Paint();
		light.setColor(getResources().getColor(R.color.puzzle_light));
		
		// Draw the minor grid lines
		for (int i = 0; i < 9; i ++){
			canvas.drawLine(0, i * height, getWidth(), i * height, light);
			canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1, hilite);
			canvas.drawLine(i * width, 0, i * width, getHeight(), light);
			canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(), hilite);
		};
		
		// Draw the major grid line
		for(int i = 0; i < 9; i++){
			if(i % 3 != 0)
				continue;
			canvas.drawLine(0, i * height, getWidth(), i * height, dark);
			canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1, hilite);
			canvas.drawLine(i * width, 0, i * width, getHeight(), dark);
			canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(), hilite);
		}
		
		//Draw the number... & define colour and style for number
		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(R.color.puzzle_foregbound));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(height * 0.75f);
		foreground.setTextScaleX(width / height);
		foreground.setTextAlign(Paint.Align.CENTER);
		
		//Draw the number in the centre of the tile
		FontMetrics fm = foreground.getFontMetrics();
		// Centring in X: using ascent/descend first
		float x = width / 2;
		// Centring in Y: using ascent/descend first
		float y = height / 2 - (fm.ascent + fm.descent) / 2;
		for( int i = 0; i < 9; i ++){
			for(int j = 0; j < 9; j++){
				canvas.drawText(this.game.getTileString(i, j), i * width + x, j * height + y, foreground);
			}
		}
		
		
		// draw the hints
		Paint hint = new Paint();
		int c[] = {getResources().getColor(R.color.puzzle_hint_0),
				getResources().getColor(R.color.puzzle_hint_1),
				getResources().getColor(R.color.puzzle_hint_2),}; 
		Rect r = new Rect();
		for(int i = 0; i < 9; i ++){
			for(int j = 0; j < 9; j ++){
				int movesleft = 9 - game.getUsedTiles(i,j).length;
				if(movesleft < c.length){
					getRect(i,j,r);
					hint.setColor(c[movesleft]);
					canvas.drawRect(r, hint);
				}
			}
		}
		
		// Draw selection ...
		Log.d(TAG, "selRect="+selRect);
		Paint selected = new Paint();
		selected.setColor(getResources().getColor(R.color.puzzle_selected));
		canvas.drawRect(selRect, selected);
	}
	
	// onKeyDown method is the way to move the selection
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		
		Log.d(TAG, "onKeyDown: keycode=" + keyCode + ", event=" + event);
		// move the selection
		switch(keyCode){
		case KeyEvent.KEYCODE_DPAD_UP:
			select(selX, selY -1);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			select(selX, selY +1);
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			select(selX - 1, selY);
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			select(selX + 1, selY);
			break;
		// to deal with input number
		case KeyEvent.KEYCODE_0:
		case KeyEvent.KEYCODE_SPACE: setSelectedTile(0); break;
		case KeyEvent.KEYCODE_1: setSelectedTile(1); break;
		case KeyEvent.KEYCODE_2: setSelectedTile(2); break;
		case KeyEvent.KEYCODE_3: setSelectedTile(3); break;
		case KeyEvent.KEYCODE_4: setSelectedTile(4); break;
		case KeyEvent.KEYCODE_5: setSelectedTile(5); break;
		case KeyEvent.KEYCODE_6: setSelectedTile(6); break;
		case KeyEvent.KEYCODE_7: setSelectedTile(7); break;
		case KeyEvent.KEYCODE_8: setSelectedTile(8); break;
		case KeyEvent.KEYCODE_9: setSelectedTile(9); break;
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER: game.showKeypadOrError(selX,selY);
		default:
			return super.onKeyDown(keyCode, event);
		
		}
		return true;
	}
	
	private void select(int a, int b){
		invalidate(selRect);
		selX = Math.min(Math.max(a, 0),8);
		selY = Math.min(Math.max(b, 0), 8);
		getRect(selX, selY, selRect); // need to create method to calculate new selection rectangle 
		invalidate(selRect);
	}
	
	// onTouchEvent method shows the same keypad
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event){
		
		if(event.getAction() != MotionEvent.ACTION_DOWN)
			return super.onTouchEvent(event);
		select((int) (event.getX() / width), (int) (event.getY() / height));
		game.showKeypadOrError(selX, selY);
		Log.d(TAG, "onTouchEvent: x " + selX + ", y " + selY);
		return true;
	}
	
	public void setSelectedTile(int t){
		if(game.setTileIfValid(selX, selY, t)){
			invalidate(); // may change hints
		}
		else {
			// number is not valid for this tile
		Log.d(TAG, "setSelectedTile: invalid: " + t);
		startAnimation(AnimationUtils.loadAnimation(game, R.anim.shake)); }
	}
	
}
