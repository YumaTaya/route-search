package routesearch;
import java.util.ArrayList;
import java.util.Calendar;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import routesearch.data.Data;
import routesearch.data.Station;


public class GUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        int fonS = 18; // font size
    	int preH = 40; // prefheight
        
    	// ラベルを作成する
        Label label = new Label("出発");
        label.setFont(new Font(fonS));
        label.setPrefHeight(preH);
        // テキストフィールドを作成する
        TextField startText = new TextField("駅1");
        // テキストフィールドを左詰めにする
        startText.setAlignment(Pos.CENTER_LEFT);
        // テキストフィールドのフォントサイズを18に、優先高を40にする
        startText.setFont(new Font(fonS));
        startText.setPrefHeight(preH);
        HBox hbox1 = new HBox(label,startText); // レイアウト1行目
        
        Label label2 = new Label("到着");
        label2.setFont(new Font(fonS));
        label2.setPrefHeight(preH);
        // テキストフィールドを作成する
        TextField goalText = new TextField("駅2");
        // テキストフィールドを左詰めにする
        goalText.setAlignment(Pos.CENTER_LEFT);
        // テキストフィールドのフォントサイズを18に、優先高を40にする
        goalText.setFont(new Font(fonS));
        goalText.setPrefHeight(preH);
        HBox hbox2 = new HBox(label2,goalText); // レイアウト2行目
        
        Label label3 = new Label("出発時刻");
        label3.setFont(new Font(fonS));
        label3.setPrefHeight(preH);
        TextField yearText = new TextField("2023");
        yearText.setAlignment(Pos.CENTER_RIGHT);
        yearText.setFont(new Font(fonS));
        yearText.setPrefHeight(preH);
        yearText.setMaxWidth(fonS*4);
        Label yearLabel = new Label("年");
        yearLabel.setFont(new Font(fonS));
        yearLabel.setPrefHeight(preH);
        
        TextField monthText = new TextField("10");
        monthText.setAlignment(Pos.CENTER_RIGHT);
        monthText.setFont(new Font(fonS));
        monthText.setPrefHeight(preH);
        monthText.setMaxWidth(fonS*3);
        Label monthLabel = new Label("月");
        monthLabel.setFont(new Font(fonS));
        monthLabel.setPrefHeight(preH);
        
        TextField dayText = new TextField("24");
        dayText.setAlignment(Pos.CENTER_RIGHT);
        dayText.setFont(new Font(fonS));
        dayText.setPrefHeight(preH);
        dayText.setMaxWidth(fonS*3);
        Label dayLabel = new Label("日");
        dayLabel.setFont(new Font(fonS));
        dayLabel.setPrefHeight(preH);
       
        goalText.setFont(new Font(fonS));
        goalText.setPrefHeight(preH);
        HBox hbox3 = new HBox(label3,yearText,yearLabel,monthText,monthLabel,dayText,dayLabel);
        
        TextField hourText = new TextField("10");
        hourText.setAlignment(Pos.CENTER_RIGHT);
        hourText.setFont(new Font(fonS));
        hourText.setPrefHeight(preH);
        hourText.setMaxWidth(fonS*3);
        Label hourLabel = new Label("時");
        hourLabel.setFont(new Font(fonS));
        hourLabel.setPrefHeight(preH);
        
        TextField minuteText = new TextField("30");
        minuteText.setAlignment(Pos.CENTER_RIGHT);
        minuteText.setFont(new Font(fonS));
        minuteText.setPrefHeight(preH);
        minuteText.setMaxWidth(fonS*3);
        Label minuteLabel = new Label("分");
        minuteLabel.setFont(new Font(fonS));
        minuteLabel.setPrefHeight(preH);
        
        HBox hbox4 = new HBox(hourText,hourLabel,minuteText,minuteLabel);
        
        Button searchBtn = new Button("検索");
        searchBtn.setFont(new Font(fonS));
        searchBtn.setMinSize(40, 40);

        HBox hbox5 = new HBox(searchBtn); //レイアウト5行目
        

        HBox hbox6 = new HBox();

        
        Data data = new Data();
        System.out.println("data load");
        searchBtn.setOnAction(event -> {
        	GUI gui = new GUI();
        	int year = Integer.parseInt(yearText.getText());
        	int month = Integer.parseInt(monthText.getText())-1;//Calendarクラスは月またぎの問題で、0から1月となっているので-1をする.
        	int day = Integer.parseInt(dayText.getText());
        	int hour = Integer.parseInt(hourText.getText());
        	int minute = Integer.parseInt(minuteText.getText());
        	Calendar date = Calendar.getInstance(); //CalendarクラスはCalendar date = new Calendar();のようには宣言せずこのように宣言するので注意.
        	date.set(year,month,day,hour,minute,0);
        	System.out.println(date.getTime());
        	gui.input(event, startText, goalText,hbox6,data,date);
        });
       
        VBox vbox = new VBox(6, hbox1, hbox2, hbox3, hbox4, hbox5, hbox6);
        
        // VBoxペインのパディングを6にする
        //vbox.setPadding(new Insets(6));
        // シーンを作成し、ペインに入れる
        Scene scene = new Scene(vbox,400,400);
        
        // ステージにVBoxペインを入れる
        primaryStage.setScene(scene);
        // ステージのタイトルバーを設定する
        primaryStage.setTitle("乗換案内");
        // ステージを表示する
        primaryStage.show();
    }
    // 検索ボタンの処理を行う
    public void input(ActionEvent event, TextField startText, TextField goalText, HBox hboxResult,Data data, Calendar date) {
        // 出発・目的地の内容を取得する
        String s = startText.getText();
        String g = goalText.getText();
        
        Station start = parseStation(s,data);
        Station goal = parseStation(g,data);
      
        if(start==null || goal==null) {
        	Label result = new Label("登録されている駅を入力してください.");
        	result.setFont(new Font(18));
        	result.setPrefHeight(40);
        	hboxResult.getChildren().clear();
        	hboxResult.getChildren().add(result);
        }else if(start.equals(goal)) {
        	Label result = new Label("出発駅と到着駅は別の駅を入力してください.");
        	result.setFont(new Font(18));
        	result.setPrefHeight(40);
        	hboxResult.getChildren().clear();
        	hboxResult.getChildren().add(result);
        }else{ //startもgoalも登録済みの場合
        	//ArrayList<ArrayList<TransferResult>> res = new ArrayList<>(); //TransferResultの2次元配列
 
        	int searchNum = 1;
        	Transfer t = new Transfer();
        	TransferResult[] res = new TransferResult[searchNum]; //現時点では配列の長さは1としているが、2,3番目に早い経路なども検索できるように拡張予定
        	res = t.minTimeResult( start,goal,date,searchNum,true,data).clone(); //minTimeResult最後の引数は出発時間の指定ならtrue,到着時間の指定ならfalse(現時点ではfalseにしても出発時間で指定されたものとして処理)
        	if(res[0]==null) {
        		Label result = new Label("経路が見つかりませんでした.");
            	result.setFont(new Font(18));
            	result.setPrefHeight(40);
            	hboxResult.getChildren().clear();
            	hboxResult.getChildren().add(result);
            	return;
        	}
        	String sYear,sMonth,sDay,sWeek,sTime;
        	String gYear,gMonth,gDay,gWeek,gTime;
        	sYear = String.valueOf(res[0].getSYear());
        	sMonth = String.valueOf(res[0].getSMonth());
        	sDay = String.valueOf(res[0].getSDay());
        	sWeek = res[0].getSWeek();
        	sTime = res[0].getSTime();
        	//Label resultDate = new Label("2023"+"/"+"10"+"/"+"14"+"(Sat)"+"...");
        	Label resultDate = new Label();
        	resultDate.setFont(new Font(18));
        	resultDate.setPrefHeight(40);
        	
        	VBox vbox = new VBox(6, resultDate);
        	
        	//下を任意の回数乗換がある場合でも最後まで出力できるようにしてください。
        	Label resultLabel1 = new Label();
        	resultLabel1.setFont(new Font(18));
        	resultLabel1.setPrefHeight(40);
        	resultLabel1.setText(res[0].getSTime()+" "+res[0].getSName()+"駅");
        	vbox.getChildren().add(resultLabel1);
	        
        	Label resultLabel2 = new Label();
        	resultLabel2.setFont(new Font(18));
        	resultLabel2.setPrefHeight(40);
        	resultLabel2.setText("↓"+res[0].getLineName());
        	vbox.getChildren().add(resultLabel2);
        	
        	Label resultLabel3 = new Label();
        	resultLabel3.setFont(new Font(18));
        	resultLabel3.setPrefHeight(40);
        	resultLabel3.setText(res[0].getGTime()+" "+res[0].getGName()+"駅");
        	vbox.getChildren().add(resultLabel3);
        	
        	
        	while(res[0].getNext()!=null) {
        		res[0] = res[0].getNext();
        	}
        	gYear = String.valueOf(res[0].getGYear());
        	gMonth = String.valueOf(res[0].getGMonth());
        	gDay = String.valueOf(res[0].getGDay());
        	gWeek = res[0].getGWeek();
        	gTime = res[0].getGTime();
        	
        	resultDate.setText(sYear+"/"+sMonth+"/"+sDay+"("+sWeek+")"+sTime+"→"
        			+gYear+"/"+gMonth+"/"+gDay+"("+gWeek+")"+gTime);
        	
        	hboxResult.getChildren().clear();
	        hboxResult.getChildren().add(vbox);
	    }
        
        return;
    }
    
    //のちのち同名駅の区別もできるようにする ComboBoxとか使うといい気がします...
    public Station parseStation(String stationName, Data allData) {
    	ArrayList<Station> allStation = allData.getAllStation();
    	for(int i=0; i<allStation.size(); i++) {
    		Station st = allStation.get(i);
    		
    		if(st.getName().equals(stationName)) {
    			System.out.println(st.getName());
    			return st; 
    		}
    	}
    	return null;
    }
    
    private String valueOf2Digits(int n) { //一桁のintの先頭に0をつけた二桁のStringでかえす
    	if(n<10){
    		return "0"+String.valueOf(n);
    	}
    	return String.valueOf(n);
    }

    public static void main(String[] args) {
        // アプリケーションを起動する
        Application.launch(args);
    }

}