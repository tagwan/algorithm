///*******************************************************************************
// * Copyright 2015 See AUTHORS file.
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// * http://www.apache.org/licenses/LICENSE-2.0
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// ******************************************************************************/
//
package com.github.tagwanj.ai.nav.triangle;

import com.github.tagwanj.ai.pfa.Connection;
import com.github.tagwanj.ai.pfa.DefaultGraphPath;
import com.github.tagwanj.math.Vector3;

/**
 * 路径点
 *
 * @author jsjolund
 */
public class TriangleGraphPath extends DefaultGraphPath<Connection<Triangle>> {
    /**
     * The start point when generating a point path for this triangle path
     */
    public Vector3 start;
    /**
     * The end point when generating a point path for this triangle path
     */
    public Vector3 end;
    /**
     * If the triangle path is empty, the point path will span this triangle
     */
    public Triangle startTri;

    /**
     * @return Last triangle in the path.
     */
    public Triangle getEndTriangle() {
        return (getCount() > 0) ? get(getCount() - 1).getToNode() : startTri;
    }
}
