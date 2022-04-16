package brickset;

import repository.Repository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.*;

/**
 * Represents a repository of {@code LegoSet} objects.
 */
public class LegoSetRepository extends Repository<LegoSet> {

    public LegoSetRepository() {
        super(LegoSet.class, "brickset.json");
    }

    public static void main(String[] args) {
        var repository = new LegoSetRepository();
        System.out.println(repository.isAtLeastOneSetWithoutSubTheme());
        repository.printDistinctTagsWhereLegoSetNameStartsWithString("F");
        System.out.println("Total pieces of the lego sets: "+repository.getTotalPiecesOfTheLegoSets());
        Map<String, Long> numberOfLegoSetsByTheme = repository.getNumberOfLegoSetsByTheme();
        System.out.println("Number of Duplo sets: "+numberOfLegoSetsByTheme.get("Duplo"));
        Map<String, String> longestNameByTheme = repository.getLongestNameByTheme();
        System.out.println("Longest name of the Duplo theme: "+longestNameByTheme.get("Duplo"));

    }

    /** Returns true if there is at least one set without a subtheme, otherwise returns false
     *
     * @return true if there is at least one set without a subtheme, otherwise returns false
     */
    public boolean isAtLeastOneSetWithoutSubTheme(){
        return getAll().stream().anyMatch(legoSet -> legoSet.getSubtheme() == null);
    }

    /** Prints out distinct lego tags sorted alphabetically with the beginning of its name specified
     *
     * @param start the beginning of the name of a lego set
     */
    public void printDistinctTagsWhereLegoSetNameStartsWithString(String start){
        getAll().stream().filter(legoSet -> legoSet.getName().startsWith(start) && legoSet.getTags() != null)
                .flatMap(legoSet -> legoSet.getTags().stream())
                .distinct()
                .sorted()
                .forEach(System.out::println);
    }

    /** Returns an int of the total pieces of all lego sets provided in the json file
     *
     * @return an int of the total pieces of all lego sets provided in the json file
     */
    public int getTotalPiecesOfTheLegoSets(){
        return getAll().stream().map(LegoSet::getPieces).filter(Objects::nonNull).reduce(0,Integer::sum);
    }

    /** Returns a map where the keys are legoset themes and the values are the number of sets in that theme
     *
     * @return a map where the keys are legoset themes and the values are the number of sets in that theme
     */
    public Map<String, Long> getNumberOfLegoSetsByTheme(){
        Map<String, Long> numberOfLegoSetsByTheme = getAll().stream().collect(groupingBy(LegoSet::getTheme, counting()));
        return numberOfLegoSetsByTheme;
    }

    /** Returns a map where the keys are legoset themes and the values are the longest name of that theme
     *
     * @return a map where the keys are legoset themes and the values are the longest name of that theme
     */
    public Map<String, String> getLongestNameByTheme(){
        Map<String, String> longestNameByTheme = getAll().stream()
                .collect(groupingBy(LegoSet::getTheme, mapping(LegoSet::getName, collectingAndThen(maxBy(Comparator.comparingInt(String::length)), Optional::get))));
        return longestNameByTheme;
    }

}
