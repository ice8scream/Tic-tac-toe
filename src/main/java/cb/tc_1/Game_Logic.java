package cb.tc_1;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Game_Logic extends AppCompatActivity implements View.OnClickListener{
   // private TextView info_t;

    /*<TextView
    android:id="@+id/info_t"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="TextView"
    android:textColor="@color/colorAccent"
    android:textSize="30sp"
    android:layout_marginRight="8dp"
    app:layout_constraintRight_toRightOf="parent"
    android:layout_marginLeft="8dp"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintHorizontal_bias="0.502"
    android:layout_marginBottom="8dp"
    app:layout_constraintTop_toTopOf="parent"
    android:layout_marginTop="8dp"
    app:layout_constraintVertical_bias="0.947" />*/

    private ImageButton[][] buttons = new ImageButton[3][3];
    private int mat_field[][] = new int[3][3];
    private ImageButton ref_b;
    private ImageButton menu_b;
    private boolean step = false;
    private int cnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        //info_t = (TextView)findViewById(R.id.info_t) ;

        buttons[0][0] = (ImageButton)findViewById(R.id.b00);
        buttons[0][1] = (ImageButton)findViewById(R.id.b01);
        buttons[0][2] = (ImageButton)findViewById(R.id.b02);

        buttons[1][0] = (ImageButton)findViewById(R.id.b10);
        buttons[1][1] = (ImageButton)findViewById(R.id.b11);
        buttons[1][2] = (ImageButton)findViewById(R.id.b12);

        buttons[2][0] = (ImageButton)findViewById(R.id.b20);
        buttons[2][1] = (ImageButton)findViewById(R.id.b21);
        buttons[2][2] = (ImageButton)findViewById(R.id.b22);

        ref_b = (ImageButton)findViewById(R.id.refr_b);
        menu_b = (ImageButton)findViewById(R.id.manu_b);

        ref_b.setOnClickListener(this);
        menu_b.setOnClickListener(this);

        for (int i = 0 ; i < 3; ++i){
            for (int j = 0 ; j < 3; ++j) {
                buttons[i][j].setOnClickListener(this);
            }
        }

        gamestart();
    }

    public void gamestart() {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                buttons[i][j].setImageResource(R.drawable.whitebar);
                buttons[i][j].setClickable(true);
                mat_field[i][j] = 0;
            }
        }
        step = false;
        cnt = 0;
    }


    @Override
    public void onClick(View view) {
        // info_t.setText(+view.getId());
        if (view.getId() == R.id.refr_b){
            gamestart();
        }else if (view.getId() == R.id.manu_b) {
            to_menu();
        }else {
            cnt++;
            int id = view.getId() - R.id.b00;
            int x = id % 3;
            int y = id / 3;
           // info_t.setText("field " + y + " " + x);
            ImageButton but = (ImageButton) findViewById(view.getId());
            if (step) {
                but.setImageResource(R.drawable.tom);
                mat_field[x][y] = 1;
            } else {
                but.setImageResource(R.drawable.cuc);
                mat_field[x][y] = 2;
            }
            step = !step;
            but.setClickable(false);

            if (is_vin(mat_field, x, y) == true) {
                Win_f(step);
            }else if(cnt == 9) {
                draw();
            }
        }
    }

    public boolean is_vin (int field[][], int x, int y){
        boolean v_n = true;

        for (int i = 0; i < 2; ++i){
            if( field[(x + i) % 3][y] != field[(x + i + 1) % 3][y]){
                v_n = false;
                break;
            }
        }
        if(v_n)return true;
        v_n = true;
        for (int i = 0; i < 2; ++i){
            if( field[x][(y + i) % 3] != field[x][(y + i + 1) % 3]){
                v_n = false;
                break;
            }
        }
        if(v_n)return true;

        if(field[1][1] != 0) {
            v_n = true;
            for (int i = 0; i < 2; ++i) {
                if (field[i][i] != field[i + 1][i + 1]) {
                    v_n = false;
                    break;
                }
            }

            if (v_n) return true;
            v_n = true;
            for (int i = 0; i < 2; ++i) {
                if (field[2 - i][i] != field[1 - i][i + 1]) {
                    v_n = false;
                    break;
                }
            }
        }

        return  v_n;
    }

    public void Win_f(boolean step){
        String ans;
        int image;
        if (step) {
            ans = "Cucumbers";
            image = R.drawable.cuc_win;
        } else {
            ans = "Tomatoes";
            image = R.drawable.tom_win;
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                buttons[i][j].setClickable(false);
            }
        }
        AlertDialog.Builder Win_stat = new AlertDialog.Builder(Game_Logic.this);
        Win_stat.setIcon(image)
                .setTitle(ans + " Wins")
                .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gamestart();
                    }
                })
                .setNegativeButton("Menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        to_menu();
                    }
                });
        AlertDialog alert = Win_stat.create();
        alert.show();
    }

    public void to_menu() {
        finish();
    }

    public void draw() {
        AlertDialog.Builder Win_stat = new AlertDialog.Builder(Game_Logic.this);
        Win_stat.setIcon(null)
                .setTitle("Draw")
                .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gamestart();
                    }
                })
                .setNegativeButton("Menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        to_menu();
                    }
                });
        AlertDialog alert = Win_stat.create();
        alert.show();
    }
}
