package routesearch.data;
import java.util.ArrayList;
import java.util.Calendar;

/* 路線の情報 */ //stationListとdiagramの型を変更しました10/27(他谷)
public class Line{
  private String name; //路線名
  //private ArrayList<Station> stationList; //路線に属する駅
  private Station[] stationList; //路線に属する駅,こっちの型の方がデータ入れやすいかも
  //private ArrayList<ArrayList<ArrayList<String>>> diagram; //路線の時刻表 
  private ArrayList<Object[][]> diagram;//路線の時刻表,こっちの型の方がデータ入れやすいかも
  private boolean isLoop = false; //環状を持つかどうか
  private ArrayList<Calendar> spDiaDateList; //特別な時刻表を用いる日
  private String loopStr; //山手線のみ使用 "外回り" もしくは "内回り",他の路線は""
  
  public Line(String name, Station[] stationList,boolean isLoop, String loopStr){
    this.name = name;
    this.stationList = stationList;
    this.diagram = null;
    this.isLoop = isLoop;
    this.spDiaDateList = new ArrayList<Calendar>();
    this.loopStr = loopStr;
  }

  //diagramに必要なデータが格納されているか確認する.データの格納の仕方が確定してないので仮.
  public boolean isComplete() {
	  return (diagram!=null);
  }

  //dateの型がわからないため、int型で代用している。
  public int diaNum(Calendar date){
    int i = 0;
    for(Calendar spDate : spDiaDateList){
      if(date==spDate){
        return i*2+4;
      }
      i = i + 1;
    }
    // holidayのライブラリが無いため、下に関数isHolidayを定義してある。
    if(isHoliday(date)){
      return 1;
    }
    // 本来date.weekday()であったが、dateの構造がわからないため、そのまま入れてある。
    return weekNum(date);

    
  }

  // 祝日の判定
  private boolean isHoliday(Calendar date){
    /* 祝日の判定関数を入れる */
    return false;
  }

  // 土日、平日の判定
  private static int weekNum(Calendar date){
    if(date.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || 
    		date.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
      return 1;
    }  
    return 0;
  }

  /* getter setter */

  public void setDiagram(ArrayList<Object[][]> diagram) {
    this.diagram = diagram;
  }

  public String getName() {
    return name;
  }

  public Station[] getStationList() {
    return stationList;
  }

  public ArrayList<Object[][]> getDiagram() {
    return diagram;
  }

  public boolean getIsLoop(){
    return isLoop;
  }

  public ArrayList<Calendar> getSpDiaDateList() {
    return spDiaDateList;
  }

  public String getLoopStr() {
    return loopStr;
  }
}