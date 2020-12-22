package mapElements;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class AnimalGenes {
    public static int genomeSize = 32;
    public static int typeOfGenes = 8;
    private final int[] genes = new int[genomeSize];
    private int[] genesCounter = new int[typeOfGenes];

    public AnimalGenes(){
        fillWithRandom();
    }

    public AnimalGenes(AnimalGenes genesA, AnimalGenes genesB){
        Random r = new Random();
        int first = r.nextInt(genomeSize-1);
        int second = r.nextInt(genomeSize - first - 1) + first + 1;

        for (int i = 0; i <= first; i++) {
            genes[i] = genesA.getGene(i);
        }
        for (int i = first + 1; i <= second; i++) {
            genes[i] = genesB.getGene(i);
        }
        for (int i = second; i < genomeSize; i++) {
            genes[i] = genesA.getGene(i);
        }
        repairGenotype();
    }

    public void fillWithRandom(){
        Random r = new Random();
        for(int i = 0; i < typeOfGenes; i++){
            genes[i] = i;
            genesCounter[i]++;
        }
        for(int i = typeOfGenes; i < genomeSize; i++){
            genes[i] = r.nextInt(typeOfGenes);
            genesCounter[genes[i]]++;
        }
        Arrays.sort(genes);
    }

    public void repairGenotype(){
        Random r = new Random();
        for(int i = 0; i < genomeSize; i++){
            genesCounter[genes[i]]++;
        }
        for(int i = 0; i < typeOfGenes; i++){
            if(genesCounter[i] == 0){
                LinkedList <Integer> moreThan1 = new LinkedList<>();
                for(int j = 0; j < typeOfGenes; j++){
                    if(genesCounter[j] > 1){
                        for(int k=0; k < genomeSize; k++){
                            if(genes[k]==j){
                                moreThan1.add(k);
                            }
                        }
                        int index = moreThan1.get(r.nextInt(moreThan1.size()));
                        genesCounter[j]--;
                        genesCounter[i]++;
                        genes[index] = i;
                        break;
                    }

                }

            }
        }
        Arrays.sort(genes);
    }

    public int getGene(int index){
        return genes[index];
    }

    public int getNumberOfGenes(int gene){
        return genesCounter[gene];
    }

    public StringBuilder showGenome(){
        StringBuilder genome = new StringBuilder("");
        for(int i=0; i<32; i++){
            genome.append(genes[i]);
        }
        return genome;
    }

}
