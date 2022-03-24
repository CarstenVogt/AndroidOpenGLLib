// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 17.3.2022

package de.thkoeln.cvogt.android.opengl_utilities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * THIS CLASS MIGHT BE USEFUL IN THE FUTURE TO EXTEND THE LIBRARIES WITH UTILITIES FOR WORKING WITH MATHEMATICAL GRAPHS OF NODES AND VERTICES.
 * IT HAS BEEN COPIED FROM THE COMPANION PROJECT FOR 2D PROPERTY ANIMATION.
 *
 * This class provides some utility methods for working with graphs, especially for displaying them on a screen.
 * A graph can be specified in two ways:<UL>
 * <LI> A HashMap: Its key set specifies the set of vertices.
 * Each key (= vertex) is mapped to a collection that specifies the adjacency list of this vertex,
 * i.e. contains all the vertices that are directly connected with the key vertex by an edge.</LI>
 * <LI> An SQLite database: It is assumed that the database includes two tables - one specifying the vertices and one specifying the edges.
 * The vertex table is required to have a column with primary keys of type INTEGER that uniquely identify the vertices.
 * The edge table is required to have two columns specifying the vertices belonging to the edges. The entries of these columns are foreign keys
 * of type INTEGER referring to the primary key column of the vertex table.</LI>
 * </UL>
 * The methods of this class work mainly on the HashMap representation. The auxiliary method <I>generateGraphHashMapFromSQL()</I> generates the
 * HashMap representation from a given SQLite database.
 */

public class GraphsUtilsCV {

   /** Method to find the components of a graph.
    *
    * @param graph The graph as a HashMap: Its key set specifies the set of vertices.
    *              Each key (= vertex) is mapped to a collection that specifies the adjacency list of this vertex,
    *              i.e. contains all the vertices that are directly connected with the key vertex by an edge.
    * @return An ArrayList with the components. Each component is represented by an ArrayList with its nodes.
    */

    public static ArrayList<ArrayList<Object>> findComponents(HashMap<Object, Collection> graph) {
       ArrayList<ArrayList<Object>> result = new ArrayList<>();
       ArrayList<Object> remainingNodes = new ArrayList<>();
       remainingNodes.addAll(graph.keySet());
       while (!remainingNodes.isEmpty()) {
           ArrayList<Object> currentComponent = new ArrayList<>();
           ArrayList<Object> toBeProcessed = new ArrayList<>();
           toBeProcessed.add(remainingNodes.remove(0));
           while (!toBeProcessed.isEmpty()) {
               Object next = toBeProcessed.remove(0);
               if (currentComponent.contains(next)) continue;
               currentComponent.add(next);
               if (graph.get(next) == null) continue;
               for (Object obj : graph.get(next))
                   toBeProcessed.add(obj);
           }
           for (Object obj : currentComponent)
               remainingNodes.remove(obj);
           result.add(currentComponent);
       }
       return result;
    }


    /** Method to generate the layout for a graph = to calculate the 2D positions of its vertices.
     * The resulting layout will be a tree starting from a vertex marked as the "root" of the tree.
     *
     * @param graph The graph as a HashMap: Its key set specifies the set of vertices.
     *              Each key (= vertex) is mapped to a collection that specifies the adjacency list of this vertex,
     *              i.e. contains all the vertices that are directly connected with the key vertex by an edge.
     * @param root The root vertex to start from
     * @param left Left border of the area where to place the vertices
     * @param top Upper border of the area where to place the vertices
     * @param right Right border of the area where to place the vertices
     * @param bottom Lower border of the area where to place the vertices
     * @param verticalSpacing The vertical spacing in the tree layout
     * @return A map mapping the vertices of the graph (as given by the key set of the graph parameter) to their positions
     */

    public static HashMap<Object, Point> placeVertices_TreeLayout(HashMap<Object, Collection> graph, Object root, int left, int top, int right, int bottom, int verticalSpacing) {
        HashMap<Object, Point> result = new HashMap();
        HashMap<Object, Collection> graphLocal = copyHashMap(graph); // local copy of the graph to work on
        completeUndirectedGraph(graphLocal);
        ArrayList verticesToPlace = new ArrayList();  // the vertices to be placed in the current round = on the current tree level
        // start from the root
        verticesToPlace.add(root);
        ArrayList nextVerticesToPlace;
        int areaWidth = right - left;
        int posY = verticalSpacing;
        while (!verticesToPlace.isEmpty()) {
            int horizontalSpacing = areaWidth / verticesToPlace.size();
            nextVerticesToPlace = new ArrayList();  // the vertices to be positioned in the next round = on the next tree level
            int i=0;
            // position the vertices in the current round = on the current tree level
            for (Iterator it = verticesToPlace.iterator(); it.hasNext();) {
                Object currentVertex = it.next();
                Collection adjacentToCurrent = graphLocal.get(currentVertex);
                if (adjacentToCurrent!=null)  // if there are vertices adjacent to the vertex currently under consideration ...
                    for (Iterator it2 = adjacentToCurrent.iterator(); it2.hasNext();) {
                        Object adjacentVertex = it2.next();
                        if (!result.keySet().contains(adjacentVertex))  // ... that have not yet been placed:
                            nextVerticesToPlace.add(adjacentVertex); // register these vertices to be placed in the next round = on the next tree level
                    }
                graphLocal.remove(currentVertex);
                int posX = (int)((i+0.5)*horizontalSpacing);
                i++;
                result.put(currentVertex,new Point(posX,posY));
            }
            // prepare positioning in the next round = on the next tree level
            posY += verticalSpacing;
            verticesToPlace = nextVerticesToPlace;
        }
        return result;
    }

    /** Method to generate the layout for a graph = to calculate the 2D positions of its vertices.
     * The resulting layout will be a balloon layout centered around a vertex marked as the root of the tree.
     * NB: Works currently only for graph that are trees.
     *
     * @param graph The graph as a HashMap: Its key set specifies the set of vertices.
     *              Each key (= vertex) is mapped to a collection that specifies the adjacency list of this vertex,
     *              i.e. contains all the vertices that are directly connected with the key vertex by an edge.
     * @param root The root vertex to start from
     * @param left Left border of the area where to place the vertices
     * @param top Upper border of the area where to place the vertices
     * @param right Right border of the area where to place the vertices
     * @param bottom Lower border of the area where to place the vertices
     * @return A map mapping the vertices of the graph (as given by the key set of the graph parameter) to their positions
     */
/*
    public static HashMap<Object, Point> placeVertices_BalloonLayout(HashMap<Object, Collection> graph, Object root, int left, int top, int right, int bottom) {
        HashMap<Object, Point> result = new HashMap();
        HashMap<Object, Collection> graphLocal = copyHashMap(graph); // local copy of the graph to work on
        // completeUndirectedGraph(graphLocal);
        // start from the root
        int centerX = left+(right-left)/2;
        int centerY = top+(bottom-top)/2;
        result.put(root,new Point(centerX,centerY));
        Collection nextVerticesToPlace = graphLocal.get(root);
        if (nextVerticesToPlace==null||nextVerticesToPlace.size()==0) return result;
        int nextWidth = (right-left)/2;
        int nextHeight = (bottom-top)/2;
        Point[] positions = de.thkoeln.cvogt.android.opengl_utilities.pointsOnCircle(centerX,centerY,(centerX-left)/2,nextVerticesToPlace.size());
        int i=0;
        for (Iterator it = nextVerticesToPlace.iterator(); it.hasNext();) {
            result.putAll(placeVertices_BalloonLayout(graphLocal,it.next(),positions[i].x-nextWidth/2,positions[i].y-nextHeight/2,positions[i].x+nextWidth/2,positions[i].y+nextHeight/2));
            i++;
        }
        return result;
    }
*/
    /** Method to generate the layout for a graph = to calculate the 2D positions of its vertices.
     * The method uses the algorithm for force-directed vertex placement.
     *
     * @param graph The graph as a HashMap: Its key set specifies the set of vertices.
     *              Each key (= vertex) is mapped to a collection that specifies the adjacency list of this vertex,
     *              i.e. contains all the vertices that are directly connected with the key vertex by an edge.
     * @param left Left border of the area where to place the vertices
     * @param top Upper border of the area where to place the vertices
     * @param right Right border of the area where to place the vertices
     * @param bottom Lower border of the area where to place the vertices
     * @return A map mapping the vertices of the graph (as given by the key set of the graph parameter) to their positions
     */

    // Source: http://www.mathematica-journal.com/issue/v10i1/contents/graph_draw/graph_draw_3.html
/*
    public static HashMap<Object, Point> placeVertices_ForceDirectedLayout(HashMap<Object, Collection> graph, int left, int top, int right, int bottom) {
        HashMap<Object, Point> result = new HashMap();
        HashMap<Object, Collection> graphLocal = copyHashMap(graph); // local copy of the graph to work on
        completeUndirectedGraph(graphLocal);
        // Initialization: Place all vertices on a circle around the center
        Point points[] = de.thkoeln.cvogt.android.propanim_utilities.GraphicsUtilsCV.pointsOnCircle(left+(right-left)/2,top+(bottom-top)/2,(right-left)/4,graphLocal.size());
        int k=0;
        for (Iterator it = graphLocal.keySet().iterator(); it.hasNext();)
            result.put(it.next(),points[k++]);
        int numberIterations = 30;
        double optimalDistance = 400;
        double relativeStrength = 0.1;
        double delta = 0.1;  // determines how far to move a vertex into the direction of the force
        for (int i=0;i<numberIterations;i++) {
            HashMap forcesX = new HashMap();  // HashMaps to store the forces currently experienced by the vertices
            HashMap forcesY = new HashMap();  // HashMaps to store the forces currently experienced by the vertices
            for (Iterator it = graphLocal.keySet().iterator(); it.hasNext();) {
                Object vertex = it.next();
                // current position of the vertex
                Point vertexPos = result.get(vertex);
                // compute the force currently experienced by the vertex
                double forceX = 0;
                double forceY = 0;
                // repulsive forces: vertices are electrically charged -> they repulse each others
                for (Iterator it2 = graphLocal.keySet().iterator(); it2.hasNext();) {
                    Object vertex2 = it2.next();
                    if (vertex==vertex2) continue;
                    Point vertex2Pos = result.get(vertex2);
                    double distance = de.thkoeln.cvogt.android.propanim_utilities.GraphicsUtilsCV.distance(vertexPos,vertex2Pos);
                    double repX = -relativeStrength*optimalDistance*optimalDistance /(distance*distance)*(vertex2Pos.x-vertexPos.x);
                    double repY = -relativeStrength*optimalDistance*optimalDistance /(distance*distance)*(vertex2Pos.y-vertexPos.y);
                    //Log.v("DEMO","rep. "+((AnimatedGuiObjectCV) vertex).getName()+" "+((AnimatedGuiObjectCV) vertex2).getName()
                    //        +": "+repX+" "+repY);
                    forceX += repX;
                    forceY += repY;
                }
                // spring forces: vertices sharing an edge are connected by a spring -> they attract each others
                for (Iterator it2 = graphLocal.get(vertex).iterator(); it2.hasNext();) {
                    Object vertex2 = it2.next();
                    Point vertex2Pos = result.get(vertex2);
                    double distance = de.thkoeln.cvogt.android.propanim_utilities.GraphicsUtilsCV.distance(vertexPos,vertex2Pos);
                    double attrX = (distance/optimalDistance)*(vertex2Pos.x-vertexPos.x);
                    double attrY = (distance/optimalDistance)*(vertex2Pos.y-vertexPos.y);
                    // Log.v("DEMO","att. "+((AnimatedGuiObjectCV) vertex).getName()+" "+((AnimatedGuiObjectCV) vertex2).getName()
                    //        +": "+attrX+" "+attrY);
                    forceX += attrX;
                    forceY += attrY;
                }
                forcesX.put(vertex,forceX);
                forcesY.put(vertex,forceY);
            }
            for (Iterator it = graphLocal.keySet().iterator(); it.hasNext();) {
                Object vertex = it.next();
                // move the vertex into the direction of the force
                Point pt = result.get(vertex);
                pt.x += delta*(double)forcesX.get(vertex);
                pt.y += delta*(double)forcesY.get(vertex);
            }

        }
        return result;
    }
*/
    /** Method to generate the layout for a bipartite graph = to calculate the 2D positions of its vertices.
     * The method starts from a root vertex which is placed at the top-left corner of the designated area.
     * The vertices adjacent to the root are placed right-bound in the lines below,
     * the vertices adjacent to them left-bound and so on.
     *
     * @param graph The graph as a HashMap: Its key set specifies the set of vertices.
     *              Each key (= vertex) is mapped to a collection that specifies the adjacency list of this vertex,
     *              i.e. contains all the vertices that are directly connected with the key vertex by an edge.
     *              It is assumed that the graph is bipartite and connected.
     * @param root Root vertex from where to start.
     * @param left Left border of the area where to place the vertices
     * @param top Upper border of the area where to place the vertices
     * @param right Right border of the area where to place the vertices
     * @param defaultObjectWidth The default width of the graphical objects to place (if a graphical object is of class AnimatedGuiObjectCV the actual width is used)
     * @param defaultObjectHeight The default height of the graphical objects to place (if a graphical object is of class AnimatedGuiObjectCV the actual height is used)
     * @param verticalSpacing The vertical spacing between the graphical objects
     * @return A map mapping the vertices of the graph (as given by the key set of the graph parameter) to their positions
     */
/*
    public static HashMap<Object, Point> placeVertices_BipartiteGraphLayout(HashMap<Object, Collection> graph, Object root, int left, int top, int right, int defaultObjectWidth, int defaultObjectHeight, int verticalSpacing) {
        HashMap<Object, Point> result = new HashMap();
        HashMap<Object, Collection> graphLocal = copyHashMap(graph); // local copy of the graph to work on
        completeUndirectedGraph(graphLocal);
        ArrayList verticesToPlace = new ArrayList();  // the vertices to be placed in the current round = on the current tree level
        boolean leftboundPlacement = true;  // shall the placement in the current round be left-bound or right-bound?
        // start from the root
        verticesToPlace.add(root);
        ArrayList nextVerticesToPlace;
        int objHeight = defaultObjectHeight;
        if (root instanceof AnimatedGuiObjectCV)
            objHeight = ((AnimatedGuiObjectCV) root).getHeight();
        int posY = top+objHeight/2+verticalSpacing/2;
        while (!verticesToPlace.isEmpty()) {
            nextVerticesToPlace = new ArrayList();  // the vertices to be positioned in the next round = on the next tree level
            int i=0;
            // position the vertices in the current round = on the current tree level
            for (Iterator it = verticesToPlace.iterator(); it.hasNext();) {
                Object currentVertex = it.next();
                Collection adjacentToCurrent = graphLocal.get(currentVertex);
                if (adjacentToCurrent!=null)  // if there are vertices adjacent to the vertex currently under consideration ...
                    for (Iterator it2 = adjacentToCurrent.iterator(); it2.hasNext();) {
                        Object adjacentVertex = it2.next();
                        if (!result.keySet().contains(adjacentVertex))  // ... that have not yet been placed:
                            nextVerticesToPlace.add(adjacentVertex); // register these vertices to be placed in the next round = on the next tree level
                    }
                graphLocal.remove(currentVertex);
                int objWidth = defaultObjectWidth;
                if (currentVertex instanceof AnimatedGuiObjectCV)
                    objWidth = ((AnimatedGuiObjectCV) currentVertex).getWidth();
                int posX;
                if (leftboundPlacement)
                    posX = left+objWidth/2;
                else
                    posX = right - objWidth/2;
                i++;
                result.put(currentVertex,new Point(posX,posY));
                objHeight = defaultObjectHeight;
                if (root instanceof AnimatedGuiObjectCV)
                    objHeight = ((AnimatedGuiObjectCV) root).getHeight();
                posY += objHeight+verticalSpacing;
            }
            // prepare positioning in the next round = on the next tree level
            verticesToPlace = nextVerticesToPlace;
            leftboundPlacement = !leftboundPlacement;
        }
        return result;
    }
*/
    /** Method to add animators to objects of class AnimatedGuiObjectsCV that form a graph.
     * The animation will start form a root vertex that will be shown first.
     * From this root vertex, its adjacent vertices will move linearly to their positions,
     * then their adjacent vertices to their positions and so on
     *
     * @param graph The graph as a HashMap: Its key set specifies the set of vertices.
     *              Each key (= vertex) is mapped to a collection that specifies the adjacency list of this vertex,
     *              i.e. contains all the vertices that are directly connected with the key vertex by an edge.
     *              Although not explicitly stated, the key type and the element type of the collection must be AnimatedGuiObjectCV.
     * @param root Root vertex from where to start the animation.
     * @param positions The target positions of the vertices.
     *              Although not explicitly stated, the key type must be AnimatedGuiObjectCV.
     * @param roundDuration The duration of a round, i.e. the time to move a group of nodes to their positions (in ms)
     * @param startDelay Delay before the animation starts (in ms)
     */
/*
    public static void animateGraphPlacement(HashMap<Object, Collection> graph, AnimatedGuiObjectCV root, HashMap<Object, Point> positions, int roundDuration, int startDelay) {
        animateGraphPlacement(graph,root,positions,1,roundDuration,startDelay);
    }
*/
    /** Method to add animators to objects of class AnimatedGuiObjectsCV that form a graph.
     * The animation will start form a root vertex that will be shown first.
     * From this root vertex, its adjacent vertices will move linearly to their positions,
     * then their adjacent vertices to their positions and so on.
     * After playing, the animators will be removed from the animator lists of the objects
     * and hence will not be replayed.
     *
     * @param graph The graph as a HashMap: Its key set specifies the set of vertices.
     *              Each key (= vertex) is mapped to a collection that specifies the adjacency list of this vertex,
     *              i.e. contains all the vertices that are directly connected with the key vertex by an edge.
     *              Although not explicitly stated, the key type and the element type of the collection must be AnimatedGuiObjectCV.
     * @param root Root vertex from where to start the animation.
     * @param positions The target positions of the vertices.
     *              Although not explicitly stated, the key type must be AnimatedGuiObjectCV.
     * @param sizeFinal The final sizes of the vertices, relative to their initial sizes (e.g.: 2.0 = double size, 0.5 = half size, 1 = no animation).
     * @param roundDuration The duration of a round, i.e. the time to move a group of nodes to their positions (in ms).
     * @param startDelay Delay before the animation starts (in ms).
     */
/*
    public static void animateGraphPlacement(HashMap<Object, Collection> graph, AnimatedGuiObjectCV root, HashMap<Object, Point> positions, double sizeFinal, int roundDuration, int startDelay) {
        HashMap<Object, Collection> graphLocal = copyHashMap(graph); // local copy of the graph to work on
        HashMap<AnimatedGuiObjectCV, Point> startPositions = new HashMap<>();  // positions from where to start the animations
        completeUndirectedGraph(graphLocal);
        // start from the root
        root.setVisible(true);
        root.setCenter(positions.get(root));
        graphLocal.remove(root);
        Collection<AnimatedGuiObjectCV> verticesToAnimate = graph.get(root);  // the vertices to be animated in the first round
        for (Iterator<AnimatedGuiObjectCV> it = verticesToAnimate.iterator(); it.hasNext();)
            startPositions.put(it.next(),root.getCenter());
        while (!verticesToAnimate.isEmpty()) {
            Collection<AnimatedGuiObjectCV> nextVerticesToAnimate = new ArrayList();  // the vertices to be animated in the next round
            // animate the vertices in the current round
            for (Iterator<AnimatedGuiObjectCV> it = verticesToAnimate.iterator(); it.hasNext();) {
                AnimatedGuiObjectCV currentVertex = it.next();
                currentVertex.setCenter(startPositions.get(currentVertex));
                currentVertex.setVisible(false);
                Animator appearanceAnim = currentVertex.addAppearanceAnimator(startDelay);
                Point target = positions.get(currentVertex);
                Animator pathAnim = currentVertex.addLinearPathAnimator(target.x,target.y,roundDuration,startDelay);
                if (sizeFinal!=1) {
                    Animator sizeAnim = currentVertex.addSizeAnimator((int) (sizeFinal * currentVertex.getWidth()), (int) (sizeFinal * currentVertex.getHeight()), roundDuration, startDelay);
                }
                // currentVertex.addBezierPathAnimator(currentVertex.getCenterX()+(target.x-currentVertex.getCenterX())/2,2000,target.x,target.y,roundDuration,startDelay);
                Collection adjacentToCurrent = graphLocal.get(currentVertex);
                if (adjacentToCurrent!=null)  // if there are vertices adjacent to the vertex currently under consideration ...
                    for (Iterator<AnimatedGuiObjectCV> it2 = adjacentToCurrent.iterator(); it2.hasNext();) {
                        AnimatedGuiObjectCV adjacentVertex = it2.next();
                        if (graphLocal.keySet().contains(adjacentVertex)) {  // ... that have not yet been animated:
                            nextVerticesToAnimate.add(adjacentVertex); // register these vertices to be placed in the next round
                            startPositions.put(adjacentVertex,new Point(target.x,target.y));
                        }
                    }
                graphLocal.remove(currentVertex);
            }
            // prepare animation in the next round
            verticesToAnimate = nextVerticesToAnimate;
            startDelay += roundDuration;
        }
    }
*/
    /** Auxiliary method to generate a HashMap representation of a graph from its SQLite representation.
     * The SQLite database is assumed to include two tables - one specifying the vertices and one specifying the edges.
     * The vertex table is required to have a column with primary keys of type INTEGER that uniquely identify the vertices.
     * The edge table is required to have two columns specifying the vertices belonging to the edges. The entries of these columns are foreign keys
     * of type INTEGER, referring to the primary key column of the vertex table.
     * The key set of the returned HashMap is the set of vertices. Each key (= vertex) is mapped to a collection that specifies the adjacency list
     * of this vertex, i.e. contains all the vertices that are directly connected with the key vertex by an edge.
     * @param database The SQLite database specifying the graph.
     * @param nameVertexTable The name of the database table specifying the vertices.
     * @param colnameVertexPRIK The name of the column of the vertex table with the primary keys of the vertices.
     * @param nameEdgeTable The name of the database table specifying the edges.
     * @param colnameVertex1FOK The name of the column of the edge table with the foreign keys of the first vertices of the edges.
     * @param colnameVertex2FOK The name of the column of the edge table with the foreign keys of the second vertices of the edges.
     * @return The corresponding HashMap, as described above.
     */

    public static HashMap<Integer, HashSet<Integer>> generateGraphHashMapFromSQL(SQLiteDatabase database, String nameVertexTable, String colnameVertexPRIK,
                                                                                 String nameEdgeTable, String colnameVertex1FOK, String colnameVertex2FOK) {
        HashMap<Integer, HashSet<Integer>> result = new HashMap<Integer, HashSet<Integer>>();
        String[] vertexProjection = { colnameVertexPRIK };
        Cursor vertexCursor = database.query(nameVertexTable,vertexProjection,null,null,null,null,null);
        if (vertexCursor != null) {
            int prikIndex = vertexCursor.getColumnIndexOrThrow(colnameVertexPRIK);
            if (vertexCursor.moveToFirst()) {
                do {
                    result.put(vertexCursor.getInt(prikIndex), new HashSet<Integer>());
                } while (vertexCursor.moveToNext());
            }
        }
        String[] edgeProjection = { colnameVertex1FOK, colnameVertex2FOK };
        Cursor edgeCursor = database.query(nameEdgeTable,edgeProjection,null,null,null,null,null);
        if (edgeCursor != null) {
            int fok1Index = edgeCursor.getColumnIndexOrThrow(colnameVertex1FOK);
            int fok2Index = edgeCursor.getColumnIndexOrThrow(colnameVertex2FOK);
            if (edgeCursor.moveToFirst()) {
                do {
                    int fok1 = edgeCursor.getInt(fok1Index);
                    int fok2 = edgeCursor.getInt(fok2Index);
                    result.get(fok1).add(fok2);
                    result.get(fok2).add(fok1);
                } while (edgeCursor.moveToNext());
            }
        }
        return result;
    }

    /** Auxiliary method to complete an undirected graph: For all edges (x,y), the corresponding edge (y,x) will be added.
     * @param graph The graph as a HashMap: Its key set specifies the set of vertices.
     *              Each key (= vertex) is mapped to a collection that specifies the adjacency list of this vertex,
     *              i.e. contains all the vertices that are directly connected with the key vertex by an edge.
     */

    public static void completeUndirectedGraph(HashMap<Object, Collection> graph) {
        for (Iterator it = graph.keySet().iterator(); it.hasNext();) {
            Object vertex = it.next();
            for (Iterator it2 = graph.get(vertex).iterator(); it2.hasNext();) {
                graph.get(it2.next()).add(vertex);
            }
        }
    }

    /** Auxiliary method to complete an undirected graph: For all edges (x,y), the corresponding edge (y,x) will be added.
     * @param graph The graph as a HashMap: Its key set specifies the set of vertices.
     *              Each key (= vertex) is mapped to a collection that specifies the adjacency list of this vertex,
     *              i.e. contains all the vertices that are directly connected with the key vertex by an edge.
     */
/*
    public static void completeUndirectedGraphAnimGuiObj(HashMap<AnimatedGuiObjectCV, Collection<AnimatedGuiObjectCV>> graph) {
        for (Iterator<AnimatedGuiObjectCV> it = graph.keySet().iterator(); it.hasNext();) {
            AnimatedGuiObjectCV vertex = it.next();
            for (Iterator it2 = graph.get(vertex).iterator(); it2.hasNext();) {
                graph.get(it2.next()).add(vertex);
            }
        }
    }
*/
    /** Auxiliary method to copy a HashMap that maps objects to collections: Also the collections are copied (into HashSets).
     * @param hashMap The HashMap to be copied.
     * @return The copy of the HashMap.
     */

    private static HashMap<Object, Collection> copyHashMap(HashMap<Object, Collection> hashMap) {
        HashMap<Object, Collection> result = new HashMap<Object, Collection>();
        for (Iterator it = hashMap.keySet().iterator(); it.hasNext();) {
            Object obj = it.next();
            result.put(obj,new HashSet(hashMap.get(obj)));
        }
        return result;
    }

    /** Auxiliary method to copy a HashMap that maps objects of class AnimatedGuiObjectsCV to Collections: Also the collections are copied (into HashSets).
     * @param hashMap The HashMap to be copied.
     * @return The copy of the HashMap.
     */
/*
    private static HashMap<AnimatedGuiObjectCV, Collection<AnimatedGuiObjectCV>> copyHashMapAnimGuiObj(HashMap<AnimatedGuiObjectCV, Collection<AnimatedGuiObjectCV>> hashMap) {
        HashMap<AnimatedGuiObjectCV, Collection<AnimatedGuiObjectCV>> result = new HashMap<AnimatedGuiObjectCV, Collection<AnimatedGuiObjectCV>>();
        for (Iterator<AnimatedGuiObjectCV> it = hashMap.keySet().iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            result.put(obj,new HashSet(hashMap.get(obj)));
        }
        return result;
    }
*/
}

