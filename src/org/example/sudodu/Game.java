package org.example.sudodu;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class Game extends Activity{
		private static final String TAG = "Sudoku";
		public static final String KEY_DIFFICULTY = "org.example.sudodu.difficulty";
		protected static final int DIFFICULTY_CONTINUE = -1; 
		public static final int DIFFICULTY_EASY = 0;
		public static final int DIFFICULTY_MEDIUM = 1;
		public static final int DIFFICULTY_HARD = 2;
		
		//private static final String PREF_PUZZLE="puzzle";
		
		private final int used[][][] = new int[9][9][];
		private int puzzle[];
		private PuzzleView puzzleView;
		
		
		
		private final String easyPuzzle ="360000000004230800000004200" 
										+ "070460003820000014500013020" 
										+ "001900000007048300000000045"; 
		private final String mediumPuzzle = "650000070000506000014000005" 
										+ "007009000002314700000700800" 
										+ "500000630000201000030000097"; 
		private final String hardPuzzle = "009000000080605020501078000" 
										+ "000000700706040102004000000" 
										+ "000720903090301080000000600"; 

		
		  @Override
		    protected void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        Log.d(TAG, "onCreate");
		        
		        int diff = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
		        puzzle = getPuzzle(diff);
		        calculateUsedTiles();
		        puzzleView = new PuzzleView(this);
		        setContentView(puzzleView);
		        puzzleView.requestFocus();

		        getIntent().putExtra(KEY_DIFFICULTY, DIFFICULTY_CONTINUE);
		  }
		  
		  @Override
		  protected void onResume(){
			super.onResume();
			//Music.play(this, R.raw.main); 
		  } 
		    
		    @Override 
		    public void onPause() { 
		    	super.onPause();
		    	Log.d(TAG, "onPause");
		    //	Music.stop(this); 
		    	
		    	// save the puzzle 
		    	// getPreferences(MODE_PRIVATE).edit().putString(PREF_PUZZLE, toPuzzleString(puzzle)).commit(); 

		    } 
		    
		  protected void showKeypadOrError(int x, int y){
			  int tiles[] = getUsedTiles(x, y);
			  if(tiles.length == 9){
				  Toast toast = Toast.makeText(this, R.string.no_moves_label, Toast.LENGTH_SHORT);
				  toast.setGravity(Gravity.CENTER, 0, 0);
				  toast.show();
			  }
			  else{
				  Log.d(TAG, "showKeypad: used=" + toPuzzleString(tiles));
				  Dialog v = new Keypad(this, tiles, puzzleView);
				  v.show();
			  }
		  }
		  
		  protected boolean setTileIfValid(int x, int y, int val){
			  
			  int tiles[] = getUsedTiles(x,y);
			  if(val  != 0){
				  for(int t : tiles){
					  if(t == val) return false;
				  }
			  }
			  setTile(x,y,val);
			  calculateUsedTiles();
			  return true;
		  }
		  
		  protected int[] getUsedTiles(int a, int b){
			  return used[a][b];
		  }
		  
		  private void calculateUsedTiles(){
			  for(int i = 0; i < 9; i++){
				  for(int j = 0; j < 9; j++){
					  used[i][j] = calculateUsedTiles(i, j);
				  }
			  }
		  }
		  
		  private int[] calculateUsedTiles(int x, int y){
			   int c[] = new int[9];
			   //horizontal
			   for(int i = 0; i < 9; i++){
				   if(i == x) 
					   continue;
				   int t = getTile(i, y);
				   if(t != 0){
					   c[t - 1] = t;
				   }
			   }
			   // vertical
			   for(int i = 0; i < 9; i++){
				   if(i == y) 
					   continue;
				   int t = getTile(x, y);
				   if(t != 0){
					   c[t - 1] = t;
				   }
			   }
			   //same cell block
			   int startx = (x / 3) * 3;
			   int starty = (y / 3) * 3;
			   for(int i = startx; i < startx + 3; i++){
				   for(int j = starty; j < starty + 3; j ++){
					   if( i == x && j == y)
						   continue;
					   int t = getTile(i, j);
					   if(t != 0)
						   c[t-1] = t;
				   }
			   }
			   
			   // compress
			   int nused = 0;
			   for(int t : c){
				   if(t != 0)
					   nused++;
			   }
			   int c1[] = new int[nused];
			   nused = 0;
			   for(int t : c){
				   if(t != 0)
					   c1[nused++] = t;
			   }
			   
			   return c1;
		  }
		  
		  private int[] getPuzzle(int diff){
			  String puz = "";
			  
			  switch(diff){
			  case DIFFICULTY_CONTINUE:
				//  puz =  getPreferences(MODE_PRIVATE).getString(PREF_PUZZLE, easyPuzzle);
				  break;
			  case DIFFICULTY_HARD:
				  puz = hardPuzzle;
				  break;
			  case DIFFICULTY_MEDIUM:
				  puz = mediumPuzzle;
				  break;
			  case DIFFICULTY_EASY:
				  puz = easyPuzzle;
				  break;
			  }
			  return fromPuzzleString(puz);
		  }
		  
		  static private String toPuzzleString(int[] puz){
			  StringBuilder buf = new StringBuilder();
			  for( int el : puz){
				  buf.append(el);
			  }
			  return buf.toString();
		  }
		  
		  static protected int[] fromPuzzleString(String str){
			  int[] puz = new int[str.length()];
			  for(int i = 0; i < puz.length; i++){
				  puz[i] = str.charAt(i) - '0';
			  }
			  return puz;
		  }
		  
		  private int getTile(int x, int y){
			  return puzzle[y * 9 + y];
		  }
		  
		  private void setTile(int x, int y, int val){
			  puzzle[y * 9 + x] = val;
		  }
		  
		  protected String getTileString(int x, int y){
			  int v = getTile(x,y);
			  if(v == 0) 
				  return "";
			  else
				  return String.valueOf(v);
		  }
		 
}
