package ru.spbstu;

import org.apache.commons.lang3.Validate;

/**
 * https://rosalind.info/problems/iprb/
 */
public class DominantProbability {


    public static Double calculateDominantProbability(String population) {
        Validate.notBlank(population);
        String[] data = population.split(" ");
        double numHomozygousDominant = Double.parseDouble(data[0]);
        double numHeterozygous = Double.parseDouble(data[1]);
        double numHomozygousRecessive = Double.parseDouble(data[2]);
        return calculateDominantProbabilityInternal(numHomozygousDominant, numHeterozygous, numHomozygousRecessive);
    }

    private static double calculateDominantProbabilityInternal(double numHomozygousDominant,
                                                       double numHeterozygous,
                                                       double numHomozygousRecessive) {
        double total = numHomozygousDominant + numHeterozygous + numHomozygousRecessive;
        double recessiveProbability = (numHomozygousRecessive / total) * ((numHomozygousRecessive - 1) / (total - 1));
        double heterozygousProbability = (numHeterozygous / total) * ((numHeterozygous - 1) / (total - 1));
        double heteroRecessiveProbability = (numHeterozygous / total) * (numHomozygousRecessive / (total - 1)) + (numHomozygousRecessive / total) * (numHeterozygous / (total - 1));

        double recessiveTotal = recessiveProbability + heterozygousProbability * (0.25F) + heteroRecessiveProbability * (.5F);
        return 1 - recessiveTotal;
    }

}
