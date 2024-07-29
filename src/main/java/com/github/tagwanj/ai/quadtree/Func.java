package com.github.tagwanj.ai.quadtree;

import com.github.tagwanj.ai.nav.polygon.Polygon;
import com.github.tagwanj.ai.quadtree.point.PointQuadTree;
import com.github.tagwanj.ai.quadtree.polygon.PolygonQuadTree;

/**
 * 功能函数
 *
 * @param <V>
 */
public interface Func<V> {
    public default void call(PointQuadTree<V> quadTree, Node<V> node) {
		// pass
    }

    public default void call(PolygonQuadTree quadTree, Node<Polygon> node) {
		// pass
    }
}
