package com.api.Comics.models;

import java.math.BigInteger;
import java.util.Map;

import lombok.Data;

@Data
public class CollectionStats {
	long countOfComics;	
	double sumOfPricePaid, sumOfValue, averagePricePaid, averageValue;
	
	public CollectionStats(Map<String, ?> stats) {
		BigInteger count = (BigInteger) stats.get("CountOfComics");
		this.setCountOfComics(count.longValue());
		this.setSumOfPricePaid((Double) stats.get("SumOfPricePaid"));
		this.setSumOfValue((Double) stats.get("SumOfValue"));
		this.setAveragePricePaid((Double) stats.get("AveragePricePaid"));
		this.setAverageValue((Double) stats.get("AverageValue"));
	}
}
