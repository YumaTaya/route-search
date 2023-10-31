package routesearch.data;

public class ConnectDia { //connect diagram
	Line line;//直通先の路線
	int dn;//平日出発なら0,平日到着なら1,休日出発なら2,休日到着なら3
	int row;//行インデックス
	int column;//列インデックス
	ConnectDia(Line line,int dn, int row, int column){
		this.line = line;
		this.dn = dn;
		this.row = row;
		this.column = column;
	}
}