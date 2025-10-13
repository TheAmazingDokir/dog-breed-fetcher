package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private int callsMade = 0;
    private Map<String, List<String>> cachedSubBreeds;
    private BreedFetcher breedFetcher;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.callsMade = 0;
        this.cachedSubBreeds = new HashMap<>();
        this.breedFetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        if (cachedSubBreeds.containsKey(breed)){
            return cachedSubBreeds.get(breed);
        }else{
            callsMade += 1;
            try {
                List<String> subBreeds = breedFetcher.getSubBreeds(breed);
                cachedSubBreeds.put(breed, new ArrayList<>(subBreeds));
                return cachedSubBreeds.get(breed);
            }catch (BreedNotFoundException e){
                throw new BreedNotFoundException(breed);
            }
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}