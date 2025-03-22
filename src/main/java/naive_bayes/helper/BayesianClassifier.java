package naive_bayes.helper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BayesianClassifier {

    private List<String[]> trainDataset;
    private Map<String, Integer> featureFrequency;
    private Map<String, Double> probabilities;
    private Map<String, Integer> classFrequency;
    private List<String> classNames;
    private String[] headers;

    public BayesianClassifier(List<String[]> dataset) {
        if (dataset == null || dataset.isEmpty()) {
            throw new IllegalArgumentException("Dataset cannot be null or empty");
        }
        this.trainDataset = dataset.subList(1, dataset.size());
        this.headers = dataset.get(0);
        this.featureFrequency = new HashMap<>();
        this.probabilities = new HashMap<>();
        this.classFrequency = new HashMap<>();
        this.classNames = new LinkedList<>();

        Set<String> classSet = new HashSet<>();
        for (String[] row : this.trainDataset) {
            String className = row[row.length - 1];
            classSet.add(className);
            classFrequency.put(className, classFrequency.getOrDefault(className, 0) + 1);
        }

        this.classNames.addAll(classSet);
    }

    public void train() {
        assert this.trainDataset != null;

        int nrofFeatures = this.trainDataset.get(0).length - 1;

        Map<String, Set<String>> nrofUniqueFeature = new HashMap<>();

        // Count the number of occurrences of each feature
        for (String[] row : this.trainDataset) {
            for (int i = 0; i < nrofFeatures; i++) {
                String className = row[row.length - 1];
                String key = headers[i] + "|" + row[i] + "|" + className;
                featureFrequency.put(key, featureFrequency.getOrDefault(key, 0) + 1);
                nrofUniqueFeature.computeIfAbsent(headers[i], k -> new HashSet<>()).add(row[i]);
            }
        }

        for (String className : this.classNames) {
            int classCount = classFrequency.get(className);
            for (Map.Entry<String, Integer> entry : featureFrequency.entrySet()) {
                String key = entry.getKey();
                if (key.endsWith(className)) {
                    double probability = (double) (entry.getValue() + 1) / (classCount + nrofUniqueFeature.get(key.split("\\|")[0]).size());
                    probabilities.put(key, probability);
                }
            }
        }
    }

    public String predict(String[] features) {
        assert features != null;

        double maxProbability = 0;
        String predictedClass = null;

        for (String className : this.classNames) {
            double probability = 1;
            for (int i = 0; i < features.length; i++) {
                String key = headers[i] + "|" + features[i] + "|" + className;
                probability *= probabilities.getOrDefault(key, 1.0);
            }
            probability *= (double) classFrequency.get(className) / this.trainDataset.size();
            if (probability > maxProbability) {
                maxProbability = probability;
                predictedClass = className;
            }
        }

        System.out.println(maxProbability);
        return predictedClass;  
    }
}
