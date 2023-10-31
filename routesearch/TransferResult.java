package routesearch;

import java.util.Calendar;

import routesearch.data.Line;
import routesearch.data.Station;

/*
TransferResult:
ある一つの路線をまたぐ、出発地(乗換駅)から目的地(乗換駅)までの経路情報
例) 柏→(常磐線)→上野→(山手線)→東京と上野で乗り換える場合、柏→上野区間と上野→東京区間の2つに分けて扱う。
　　この例における柏→上野区間は、start = kashiwa, goal = ueno, ..., isFirstTransfer = true, line = jobanLIneRapid,...
　　上野→東京区間は、dep = ueno, arr = tokyo, ..., isFirstTransfer = false, ...
*/
public class TransferResult {
	private Station start,goal;
	private Calendar date;
	private int startTime,goalTime;
	private boolean isFirstTransfer;
	private Line line;
	private Station terminal;
	private String terminalStr;
	private int transferMin = -1;
	private int startNum,goalNum;
	private boolean isConnect;
	private boolean isCompetTransfer;
	private TransferResult next = null; //次の乗換経路
	private Calendar startDate;
	private Calendar goalDate;
	
	public TransferResult(Station start, Station goal, Calendar date, int startTime, int goalTime, 
				boolean isFirstTransfer, Line line, Station terminal, int transferMin, 
				int startNum, int goalNum, boolean isConnect, boolean isCompetTransfer){
		 	/*
		 	start, goal: 出発駅、到着駅
		 	date: 日付
		 	startTime, goalTime: 列車の出発時間、到着時間。 0:00:00～2:59:59は24:00:00から26:59:59で格納
		 	isFirstTransfer: 出発駅から次乗り換えるまでの最初の路線のみtrue,それ以降の路線はfalse
		 	line: 何線か
    		terminal: この列車の終着駅(終着がない場合はnullを受ける)
    		trensferMin: start駅で前の路線からこの路線に乗り換えるまでの時間(分)、設定しない場合は-1
    		startNum: 何番線から出発するか。設定しない場合は-1
    		goalNum: 何番線に到着するか。設定しない場合は-1
    		isConnect: 直前の路線と違うが直通運転扱いであるならtrue
    		isCompetTransfer: 直後のTransferResultとは別の会社の路線に乗り換えてきた場合true、今回の目標ではJRしか扱わないので常にfalseをもらう
		 	*/
			this.start = start;
			this.goal = goal;
			this.date = date;
			this.startTime = startTime;
			this.goalTime = goalTime;
			this.isFirstTransfer = isFirstTransfer;
			this.line = line;
			this.terminal = terminal;
			if(!isFirstTransfer) {//乗換2路線目以降の場合
				if(transferMin < start.getTransferMin()) {
					this.transferMin = goal.getTransferMin(); 
				}else {
					this.transferMin = transferMin;
				}
			}
			
			if(terminal != null) {
				String tail = "";
				if(line.getIsLoop()) {
					tail = "止";
				}else {
					tail = "行";
				}
				this.terminalStr = "("+terminal.getName()+tail+")";
			}else {
				this.terminalStr = "("+line.getLoopStr()+")";
			}
			this.startNum = startNum;
			this.goalNum = goalNum;
			this.isConnect = isConnect;
			this.isCompetTransfer = isCompetTransfer;
			setDate();
	}
	
	private Calendar getDate(int time) {
		int year = date.get(Calendar.YEAR);
    	int clMonth = date.get(Calendar.MONTH);
    	int day = date.get(Calendar.DATE);
		int hour,minute,second;
    	
		second = time % 100;
    	
    	time = (time-second)/100;
    	minute = time % 100;
    	
    	time = (time-minute)/100;
    	hour = time % 100; //25時なども1時などにせずそのまま
    	
    	//CalendarクラスはCalendar date = new Calendar();のようには宣言せずこのように宣言するので注意.
    	Calendar date = Calendar.getInstance(); 
    	date.set(year,clMonth,day,hour,minute,second);
    	return date;
	}
	
	private void setDate() {
		startDate = getDate(startTime);
		goalDate = getDate(goalTime);
	}
	
	private String stringValueOf(int week) {
		switch (week) { 
	    case Calendar.SUNDAY:     // Calendar.SUNDAY:1 
	        //日曜日
	        return "日";
	    case Calendar.MONDAY:     // Calendar.MONDAY:2
	        //月曜日
	        return "月";
	    case Calendar.TUESDAY:    // Calendar.TUESDAY:3
	        //火曜日
	        return "火";
	    case Calendar.WEDNESDAY:  // Calendar.WEDNESDAY:4
	        //水曜日
	    	return "水";
	    case Calendar.THURSDAY:   // Calendar.THURSDAY:5
	        //木曜日
	    	return "木";
	    case Calendar.FRIDAY:     // Calendar.FRIDAY:6
	        //金曜日
	    	return "金";
	    case Calendar.SATURDAY:   // Calendar.SATURDAY:7
	        //土曜日
	    	return "土";
	    default:
	    	return "";
		}
	}
	
	private String valueOf2Digits(int n) {
    	if(n<10){
    		return "0"+String.valueOf(n);
    	}
    	return String.valueOf(n);
    }
	
	public boolean isFirstTransfer() {
		return isFirstTransfer;
	}
	
	public boolean isConnect() {
		return isConnect;
	}
	
	public boolean isCompetTransfer() {
		return isCompetTransfer;
	}
	
	//getter
	public Station getStart() {
		return start;
	}
	
	public Station getGoal() {
		return goal;
	}
	
	public int getStartTime() {
		return startTime;
	}
	
	public int getGoalTime() {
		return goalTime;
	}
	
	public Line getline() {
		return line;
	}
	
	public Station getTerminal() {
		return terminal;
	}
	
	public String getTerminalStr() {
		return terminalStr;
	}
	
	public int getTransferMin() {
		return transferMin;
	}
	
	public int getStartNum() {
		return startNum;
	}
	
	public int getGoalNum() {
		return goalNum;
	}
	
	public TransferResult getNext() {
		return next;
	}
	
	public int getSYear() {
		return startDate.get(Calendar.YEAR);
	}
	
	public int getSMonth() {
		return startDate.get(Calendar.MONTH)+1;
	}
	
	public int getSDay() {
		return startDate.get(Calendar.DATE);
	}
	
	public int getSWeekNum() {
		return startDate.get(Calendar.DAY_OF_WEEK);
	}
	
	public String getSWeek() {
		return stringValueOf(startDate.get(Calendar.DAY_OF_WEEK));
	}
	
	public int getSHour() {
		return startDate.get(Calendar.HOUR);
	}
	
	public int getSMinute() {
		return startDate.get(Calendar.MINUTE);
	}
	
	public String getSTime() {
		return valueOf2Digits(startDate.get(Calendar.HOUR_OF_DAY))+":"+valueOf2Digits(startDate.get(Calendar.MINUTE));
	}
	
	public Calendar getSDate() {
		return startDate;
	}
	
	public String getSName() {
		return start.getName();
	}
	
	public int getGYear() {
		return goalDate.get(Calendar.YEAR);
	}
	
	public int getGMonth() {
		return goalDate.get(Calendar.MONTH)+1;
	}
	
	public int getGDay() {
		return goalDate.get(Calendar.DATE);
	}
	
	public int getGWeekNum() {
		return goalDate.get(Calendar.DAY_OF_WEEK);
	}
	
	public String getGWeek() {
		return stringValueOf(goalDate.get(Calendar.DAY_OF_WEEK));
	}
	
	public int getGHour() {
		return goalDate.get(Calendar.HOUR_OF_DAY);
	}
	
	public int getGMinute() {
		return goalDate.get(Calendar.MINUTE);
	}
	
	public String getGTime() {
		return valueOf2Digits(goalDate.get(Calendar.HOUR))+":"+valueOf2Digits(goalDate.get(Calendar.MINUTE));
	}
	
	public Calendar getGDate() {
		return goalDate;
	}
	
	public String getGName() {
		return goal.getName();
	}
	
	public String getLineName() {
		return line.getName()+" "+terminalStr;
	}
	
	//setter
	public void setNext(TransferResult next) {
		this.next = next;
	}
}