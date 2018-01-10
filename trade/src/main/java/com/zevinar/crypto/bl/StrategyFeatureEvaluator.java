package com.zevinar.crypto.bl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zevinar.crypto.impl.SimpleStrategy;
import com.zevinar.crypto.interfcaes.AbstractSimulationStrategy;
import com.zevinar.crypto.interfcaes.IStrategyFeature;
import com.zevinar.crypto.strategy.impl.BasicTradeFeature;
import com.zevinar.crypto.utils.datastruct.Wrapper;

public class StrategyFeatureEvaluator {
	protected static final Logger LOG = LoggerFactory.getLogger(StrategyFeatureEvaluator.class);
	private StrategySimulator sim = new StrategySimulator();
	
	
	public static void main(String[] args){
		StrategyFeatureEvaluator eval = new StrategyFeatureEvaluator();
		eval.eveluateBasicTradeFeature();
	}
	
	
	public void eveluateBasicTradeFeature(){
		List<Double> sellProfits = Arrays.asList(0.1,0.3);
		sim.setNumOfDays(1);
		SimpleStrategy strategy = new SimpleStrategy();
		strategy.removeAllStrategyFeature();
		
		List<IStrategyFeature> features = new ArrayList<>();
		 for( int i = 0; i < sellProfits.size(); i++ ){
			 BasicTradeFeature feature = new BasicTradeFeature();
			 feature.setBidDiscount(sellProfits.get(i));
			 features.add(feature);
		 }
		 evaluate(features, strategy);
	}
	public IStrategyFeature evaluate(List<IStrategyFeature> features, AbstractSimulationStrategy strategy) {
		Wrapper<ImmutablePair<IStrategyFeature, Double>> best = new Wrapper<>();
		features.stream().forEach(feature -> evalSingleFeature(feature, best, strategy));
		LOG.info("Best Strategy Feature balance is:{}, Feature Details:{}", best.getInnerElement().getRight(),  best.getInnerElement().getLeft().toString());
		return best.getInnerElement().getLeft();
	}
	
	private void evalSingleFeature(IStrategyFeature feature, Wrapper<ImmutablePair<IStrategyFeature, Double>> best, AbstractSimulationStrategy strategy){
		strategy.getSimExchangeHandler().reset();
		strategy.addStrategyFeature(feature);
		strategy.init();
		sim.runSimulation(strategy);
		double balanceSum = strategy.getSimExchangeHandler().getBalanceSum();
		if( best.isEmpty() ){
			best.setInnerElement(new ImmutablePair<IStrategyFeature, Double>(feature, balanceSum));
		}
		else if ( balanceSum > best.getInnerElement().getRight()){
			LOG.info("Strategy Feature is replaced with Better One: balance for new Feature is:{}, Feature Details:{}", balanceSum, feature.toString());
			best.setInnerElement(new ImmutablePair<IStrategyFeature, Double>(feature, balanceSum));
		}
		strategy.removeStrategyFeature(feature);
	}
}
