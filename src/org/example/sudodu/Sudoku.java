package org.example.sudodu;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

public class Sudoku extends ActionBarActivity implements OnClickListener{

	private static final String TAG = "Sudoku";
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        
        // set up click listener for all the buttons
        View continueButton = findViewById(R.id.continue_btn);
        continueButton.setOnClickListener((OnClickListener) this);
        View newButton = findViewById(R.id.new_btn);
        newButton.setOnClickListener((OnClickListener) this);
        View aboutButton = findViewById(R.id.about_btn);
        aboutButton.setOnClickListener((OnClickListener) this);
        View exitButton = findViewById(R.id.exit_btn);
        exitButton.setOnClickListener((OnClickListener) this);
    }
    
    @Override 
    protected void onResume() { 
    	super.onResume(); 
        //Music.play(this, R.raw.main); 
    } 
    
    @Override 
    public void onPause() { 
    	super.onPause(); 
    //	Music.stop(this); 
    } 


    
    public void onClick(View v){
		switch(v.getId()){
		case R.id.about_btn:
			Intent i = new Intent(this, About.class);
			startActivity(i);
			break;
		case R.id.continue_btn:
			startGame(Game.DIFFICULTY_CONTINUE);
			break;
		case R.id.new_btn:
			openNewGameDialog();
			break;
		case R.id.exit_btn:
			finish();
			break;
		}
	}
   
    // to open new dialog of difficulty option
    private void openNewGameDialog(){
    	new AlertDialog.Builder(this)
    		.setTitle(R.string.new_game_title)
    		.setItems(R.array.difficulty,new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int i) {
					startGame(i);
					
				}
			})
			.show();
    		
    	
    }
    

    private void startGame(int i) {
    	if (i==-1) {
    		Log.d(TAG, "continue our previous game");
    	} 
    	else {
    		Log.d(TAG, "clicked on " + getResources().getStringArray(R.array.difficulty)[i]);
    	}
    	Intent intent = new Intent(Sudoku.this, Game.class);
    	intent.putExtra(Game.KEY_DIFFICULTY, i);
    	startActivity(intent);
    	}

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
    	//getMenuInflater().inflate(R.menu.sudoku, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       // int id = item.getItemId();
        //if (id == R.id.action_settings) {
          //  return true;
       // }
        //return super.onOptionsItemSelected(item);#
        switch (item.getItemId()) {
       case  R.id.settings: // only setting
        startActivity(new Intent(this, Prefs.class));
        return true;
        }
        return false;
    }
}
