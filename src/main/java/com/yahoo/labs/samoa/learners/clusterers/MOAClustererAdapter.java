package com.yahoo.labs.samoa.learners.clusterers;

/*
 * #%L
 * SAMOA-MOA
 * %%
 * Copyright (C) 2013 SAMOA-MOA
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.github.javacliparser.ClassOption;
import com.github.javacliparser.Configurable;
import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.Instances;
import com.yahoo.labs.samoa.instances.InstancesHeader;
import com.yahoo.labs.samoa.moa.cluster.Cluster;
import com.yahoo.labs.samoa.moa.cluster.Clustering;
import java.util.Random;
import moa.clusterers.clustream.Clustream;

/**
 * Base class for moa classifiers.
 */
public class MOAClustererAdapter implements LocalClustererAdapter, Configurable {

    /**
     *
     */
    private static final long serialVersionUID = 4372366401338704353L;
    
    public ClassOption learnerOption = new ClassOption("learner", 'l',
            "Clusterer to train.", moa.clusterers.Clusterer.class, Clustream.class.getName());
    /**
     * The learner.
     */
    protected moa.clusterers.Clusterer learner;
    
    /**
     * The is init.
     */
    protected Boolean isInit;
    
    /**
     * The dataset.
     */
    protected Instances dataset;

    @Override
    public void setDataset(Instances dataset) {
        this.dataset = dataset;
    }

    /**
     * Instantiates a new learner.
     *
     * @param learner the learner
     * @param dataset the dataset
     */
    public MOAClustererAdapter(Instances dataset, ClassOption learnerOption) {
        this.learnerOption = (ClassOption) learnerOption.copy();
        this.isInit = false;
        this.dataset = dataset;
        
    }

    /**
     * Instantiates a new learner.
     *
     */
    public MOAClustererAdapter() {
        this.isInit = false;
        //this.dataset = dataset;
    }

    /**
     * Creates a new learner object.
     *
     * @return the learner
     */
    @Override
    public MOAClustererAdapter create() {
        MOAClustererAdapter l = new MOAClustererAdapter(dataset, learnerOption);
        if (dataset == null) {
            System.out.println("dataset null while creating");
        }
        return l;
    }

    /**
     * Trains this classifier incrementally using the given instance.
     *
     * @param inst the instance to be used for training
     */
    @Override
    public void trainOnInstance(Instance inst) {
        if (this.isInit == false) {
            this.isInit = true;
            this.learner = ((moa.clusterers.Clusterer) this.learnerOption.getValue()).copy();
            InstancesHeader instances = new InstancesHeader(dataset);
            this.learner.setModelContext(instances);
            this.learner.prepareForUse();
        }
        if (inst.weight() > 0) {
            inst.setDataset(dataset);
            learner.trainOnInstance(inst);
        }
    }

    /**
     * Predicts the class memberships for a given instance. If an instance is
     * unclassified, the returned array elements must be all zero.
     *
     * @param inst the instance to be classified
     * @return an array containing the estimated membership probabilities of the
     * test instance in each class
     */
    @Override
    public double[] getVotesForInstance(Instance inst) {
        double[] ret;
        inst.setDataset(dataset);
        if (this.isInit == false) {
           ret = new double[dataset.numClasses()];
        } else {
            ret = learner.getVotesForInstance(inst);
        }
        return ret;
    }

    /**
     * Resets this classifier. It must be similar to starting a new classifier
     * from scratch.
     *
     */
    @Override
    public void resetLearning() {
        learner.resetLearning();
    }

    public boolean implementsMicroClusterer() {
        return this.learner.implementsMicroClusterer();
    }

    public Clustering getMicroClusteringResult() {
        return newSamoaClustering(this.learner.getMicroClusteringResult());
    }
        
    public Clustering newSamoaClustering(moa.cluster.Clustering moaClustering){
        Clustering samoaClustering = new Clustering();
        //Convert clusters from moa to samoa
        moa.cluster.Cluster[] moaClusters = (moa.cluster.Cluster[]) moaClustering.getClustering().toArray();
        for (moa.cluster.Cluster moaCluster : moaClusters) {
            samoaClustering.add(new samoaCluster(moaCluster));
        }
        return samoaClustering;
    }
    
    public Instances getDataset() {
        return this.dataset;
    }
    
    public class samoaCluster extends Cluster{

        public samoaCluster(moa.cluster.Cluster moaCluster) {
            this.moaCluster = moaCluster;
        }
        
        moa.cluster.Cluster moaCluster;

        @Override
        public double[] getCenter() {
            return moaCluster.getCenter();
        }

        @Override
        public double getWeight() {
           return moaCluster.getWeight();
        }

        @Override
        public double getInclusionProbability(Instance instance) {
            return moaCluster.getInclusionProbability(instance);
        }

        @Override
        public Instance sample(Random random) {
           return moaCluster.sample(random);
        }
        
    }

}
