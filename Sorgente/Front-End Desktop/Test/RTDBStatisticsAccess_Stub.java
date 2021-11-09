package Test;

import data.RTDBStatisticsAccess;

public class RTDBStatisticsAccess_Stub extends RTDBStatisticsAccess {
	
	@Override
	public int[] fetchStats () {
		return new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8};	
	}
	
}
