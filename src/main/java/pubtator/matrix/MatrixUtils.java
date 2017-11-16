package pubtator.matrix;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class MatrixUtils {
	static <R, C> Map<R, TObjectIntMap<C>> reduce(Map<R, TObjectIntMap<C>> matrix,
                                                  int minEntityPairCount, int minFeatureCount,
                                                  int maxEntityPairCount, int maxFeatureCount) {
		Set<R> rowsToRemove = new HashSet<>();
		Set<C> colsToRemove = new HashSet<>();
		matrix.keySet().stream().filter(row -> (
				(matrix.get(row).size() < minEntityPairCount) || (matrix.get(row).size() > maxEntityPairCount))).
				forEach(rowsToRemove::add);
		TObjectIntMap<C> featureCounts = new TObjectIntHashMap<>();
		for (R row : matrix.keySet()) {
			for (C feature : matrix.get(row).keySet()) {
				featureCounts.adjustOrPutValue(feature, 1, 1);
			}
		}
		featureCounts.forEachEntry((feature, count) -> {
			if (count < minFeatureCount || count > maxFeatureCount) {
				colsToRemove.add(feature);
			}
			return true;
		});
		System.out.println("toremove: " + rowsToRemove.size() + "\t" + colsToRemove.size());
		Map<R, TObjectIntMap<C>> newMatrix = new HashMap<>();
		matrix.forEach((row, featureCountMap) -> {
			if (rowsToRemove.contains(row)) {
				return;
			}
			newMatrix.put(row, new TObjectIntHashMap<>());
			featureCountMap.forEachEntry((feature, count) -> {
				if (colsToRemove.contains(feature)) {
					return true;
				}
				newMatrix.get(row).put(feature, count);
				return true;
			});
		});
		if (rowsToRemove.isEmpty() && colsToRemove.isEmpty()) {
			return matrix;
		}
		return reduce(newMatrix, minEntityPairCount, minFeatureCount, maxEntityPairCount, maxFeatureCount);
	}

	static <R, C> Map<R, TObjectIntMap<C>> rowReduceRandom(Map<R, TObjectIntMap<C>> matrix, int nRows) {
		List<R> rows = new ArrayList<>(matrix.keySet());
		Collections.shuffle(rows);
		rows = rows.subList(0, Math.min(nRows, matrix.size())); // can't have more than the original num rows
		Map<R, TObjectIntMap<C>> newMatrix = new HashMap<>();
		rows.forEach(r -> newMatrix.put(r, matrix.get(r)));
		return newMatrix;
	}
}
