package com.github.tagwanj.ai.nav.polygon;

import com.github.tagwanj.ai.pfa.Connection;
import com.github.tagwanj.ai.pfa.DefaultGraphPath;
import com.github.tagwanj.math.Vector3;

/**
 * 多边形路径点
 */
public class PolygonGraphPath extends DefaultGraphPath<Connection<Polygon>> {
    public Vector3 start;
    public Vector3 end;
    public Polygon startPolygon;

    public Polygon getEndPolygon() {
        return (getCount() > 0) ? get(getCount() - 1).getToNode() : startPolygon;
    }

}
