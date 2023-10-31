package routesearch.data;
import java.util.ArrayList;
/*
class Station {
	String name;
	ArrayList<Station> nextSt = new ArrayList<Station>();
	ArrayList<StationBranch> branch = new ArrayList<StationBranch>();
	ArrayList<Integer> numArray = new ArrayList<Integer>();
	int x = -1;
	int y = -1;
	int transferMinute = 3; // 乗り換え時間の初期値は3分とする
	ArrayList<Line> lineArray = new ArrayList<Line>();
 
	Station(String stationName){
		name = stationName;
	}
	
	public boolean is_complete() {
		return nextSt.size()>=1 && branch.size()>=1 && numArray.size()>=1 &&
				x >=0 && y>=0 && transferMinute>=0;
	}
}


class Line {
	String name;
	boolean isLoop = false;
	//diagram
	ArrayList<Calendar> spDiaDate = new ArrayList<>();
	String loopStr = "";
	
	Line(String lineName){
		name = lineName;
	}
}

class StationBranch {
	
}
*/
public class Data { //アプリケーション起動時に1回きり呼び出される
	private ArrayList<Station> allStation = new ArrayList<Station>();
	
	public Data(){
		ArrayList<Station> allStation = new ArrayList<Station>();
		//ここから...
		
		Station st1= new Station("駅1");
		allStation.add(st1);
		Station st2= new Station("駅2");
		allStation.add(st2);
		Station st3= new Station("駅3");
		allStation.add(st3);
		Station st4= new Station("駅4");
		allStation.add(st4);
		Station st5= new Station("駅5");
		allStation.add(st5);
		
		Station[] stLine1In = {st1,st2};
		Line line1In = new Line("路線1",stLine1In, false,"");//末尾について、上りはin,下りはoutなどなんでもよいので区別する
		
		Station[] stLine1Out = {st2,st1};
		Line line1Out = new Line("路線1",stLine1Out, false,"");
		
		st1.addLine(line1In);
		st1.addLine(line1Out);
		
		st2.addLine(line1In);
		st2.addLine(line1Out);
		
		Object[][] line1InWeekStart =
			{{"列車番号","407B","463C","617B","617A"},
			{"列車名","普通","普通","普通","普通"},
			{"設備","","","",""},
			{"運転日","全日","全日","全日","全日"},
			{"前の区間","","","",""},
			{"始発","駅1","駅1","駅1","駅1"},
			{"駅1","60100","61000","61900","185000"},
			{"駅2","","","",""},
			{"終着","駅2","駅2","駅2","駅2"},
			{"次の区間","","","",""}};
		//ここまで平日出発ダイヤ、各タプルの要素数は全て同じ
		Object[][] line1InWeekGoal =
			{{"列車番号","407B","463C","617B","617A"},
			{"列車名","普通","普通","普通","普通"},
			{"設備","","","",""},
			{"運転日","全日","全日","全日","全日"},
			{"前の区間","","","",""},
			{"始発","駅1","駅1","駅1","駅1"},
			{"駅1","","","",""},
			{"駅2","60500","61600","62200","185400"},
			{"終着","駅2","駅2","駅2","駅2"},
			{"次の区間","","","",""}};
		Object[][] line1InHoliStart =
			{{"列車番号","407B","463C","617A"},
			{"列車名","普通","普通","普通"},
			{"設備","","",""},
			{"運転日","全日","全日","全日"},
			{"前の区間","","",""},
			{"始発","駅1","駅1","駅1"},
			{"駅1","70100","185000","245000"},
			{"駅2","","",""},
			{"終着","駅2","駅2","駅2"},
			{"次の区間","","",""}};
		Object[][] line1InHoliGoal =
			{{"列車番号","407B","463C","617A"},
			{"列車名","普通","普通","普通"},
			{"設備","","",""},
			{"運転日","全日","全日","全日"},
			{"前の区間","","",""},
			{"始発","駅1","駅1","駅1"},
			{"駅1","","",""},
			{"駅2","70500","185400","245400"},
			{"終着","駅2","駅2","駅2"},
			{"次の区間","","",""}};
		/* 直通運転の場合は以下の様に前の区間、次の区間の行にConnectDiaを与える
		 * 渡す4つの引数はpythonで説明したときと同様
		 Object[][] line1InWeekStart =
			{{"列車番号","407B","463C","617B","617A"},
			{"列車名","普通","普通","普通","普通"},
			{"設備","","","",""},
			{"運転日","全日","全日","全日","全日"},
			{"前の区間","","","",""},
			{"始発","駅1","駅1","駅1","駅1"},
			{"駅1","60100","61000","61900","185000"},
			{"駅2","","61400","",""},
			{"終着","駅2","駅5","駅2","駅2"},
			{"次の区間","",new ConnectDia(line2,0,3,10),"",""}}; 
			//直通運転で別のdiagramをまたがる場合、その列車のdiagramの始発の時刻が格納されているdiagramを指せるように
			 * 4要素(Line,同じダイヤ(この例では同じ平日出発の0)int,行インデックス,列インデックス)をインスタンスにする
			 * (駅1,駅2,と続き駅3から完全に次の路線に直通して切り替わり,駅4,終着駅5の順に停車する場合、
			 * 直通先line2の同じ列車の駅3の発車時刻が格納されているインデックスを指定する
			 * （line2の駅2の発車時刻のインデックスでないことに注意！）)
			 * 実際GUIには
			 * -----------
			 * 駅1
			 * ↓路線1
			 * 駅2　(直通運転)
			 * ↓路線2
			 * 駅5
			 * -----------
			 * などと表示される
		 */
		
		ArrayList<Object[][]> line1InDia = new ArrayList<>();
		line1InDia.add(line1InWeekStart);
		line1InDia.add(line1InWeekGoal);
		line1InDia.add(line1InHoliStart);
		line1InDia.add(line1InHoliGoal);
		
		line1In.setDiagram(line1InDia);
		
		
		//...ここまではあくまでサンプルなので最終的には書き換えて、検索対象の駅をすべて格納する。完成するまではサンプルデータも残す
		this.allStation = allStation;
	}
	
	public ArrayList<Station> getAllStation(){
		//登録されている駅全てのArrayListを取得
		return allStation;
	}
}