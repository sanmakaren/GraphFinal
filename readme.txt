readme.txt
Karen Santamaria
Final Project

I did the extension where you find the shortest path between two vertices.

I think that using the GraphGUI is pretty straight forward. When in doubt, click on a button and the instructions will be
displayed for it (except Clear Graph which I hope is self-explanatory)

Here are some things that are not explicitly stated in the GUI:
~In order to return the graph back to normal the graph after traversing though it just click on any button (except Clear Graph)
or entering a space
~When finding the shortest cost between two vertices, the cost is displayed in the instructions JLabel
~You can name a vertex before adding it if you type textfield and then click where you want to add the vertex
~You can set the distance of an edge before adding it if you type in the textfield before adding the edge

I tend to make too many fields when creating programs so I made an effort to keep variables as local as I can. It took a
lot of effort! Especially when doing the dijkstra algorithm my immediate impulse when I felt like I needed to
return a HashMap of distances and a HashMap of homeward vertices was to create two fields and store it there. I knew that
was wrong from reading feedback from my previous assignments. In the end, I decided to compromise my desire to return
two things at once by creating a "pair" class that contains the distance and homeward vertex. I made it a nest class inside
of Graph mainly because it was the convenient, in Graph I don't have to worry or check that the homeward vertex is of the
same type as the "key" vertex that's in the HashMap. This is important because I have to do comparisons when finding the
shortest path between two vertices (an extension I did for the assignment).

For the Dijkstra algorithm I originally return an Array rather than a HashMap but later I decided to just use a HashMap.
I know that HashMap is slower than an array because accessing a value is O(1) but the output of a HashMap made much more
sense to me than an array because from it you can understand exactly which Vertex goes with which distance and homeward
vertex. I think that even it's less efficient, on principle it makes the most sense.

Even though I had a setData method in the Vertex and Edge classes I instead decided to do "getData().setThing(thing)"
The reason for this is because is I used the setData method I would have to do something like
"setData(new Thing(color, string, point))" and it seemed weird to be setting an existing Vertex/Edge to a "new Thing"
when I only wanted to change one component of that "thing." I think it's a safe operation though because it doesn't change
the Vertex itself, it only changes the data inside of the vertex.

I made it so that no vertex could have the same name. I did this because even though vertices are mostly defined by their
location, it would be confusing especially when reading in a file, don't want to reference vertices by their position,
it's more practical to reference them by their name.

I made it EdgeData extend Number so that I could access "number data" by checking is something is an instance of Number
and if it is I get the doubleValue. This is important when computing dijkstra distances since it is possible that an edge
is composed of a different data since its generic but it is important that if the there is some number contained within the
Edge data that I can get the number. Also, with this method getNumData, when my edge data isn't an instance of number
I can just return 0 and still do the Dijkstra algorithm. This solves the issue where I have to make the method that goes
the dijkstra algorithm static inside of Graph.java in order to restrict its use which is a usable solution but not a very
good one.

I don't think I really made any major design choices outside of implementing Dijkstra. I created EdgeData and VertexData
because I wanted my edges and vertices to have a color attached to them as along with the other data they need. Inside of
GraphCanvas I decided to make the default colors, and vertex size "public static final." I was more inclined to make them
"private static final" but after some googling it seemed like the preferred way to have constants is as "public static final"

Inside of the class CostHomePair (I had some trouble naming it) I made a method that manipulated all the fields inside of
CostHomePair (aka cost and homeward vertex) the reason I did this instead of making is new CostHomePair is mainly because
it felt it would waste memory to be creating a new CostHomePair every time in my for loop when really all I had to do was
manipulate the current one.

Overall, fun assignment. Great class. Gruelling. Glad to be done!