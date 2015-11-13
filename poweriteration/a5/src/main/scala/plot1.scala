import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.clustering.{PowerIterationClustering, PowerIterationClusteringModel}
import org.apache.spark.mllib.linalg.Vectors
import breeze.linalg._
import breeze.plot._
//import scalala.library.Plotting._
//import scalala.tensor.dense.DenseVector
//import java.awt.Color
//import scalala.library.plotting.GradientPaintScale


object plot1 {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("PIC")
    val sc = new SparkContext(conf)

//val x = linspace(0.0,1.0)

//----------------------------------------------------------------------------

 val file = sc.textFile("/home/madhura/spark/hw5/blobs.txt")
 var xyZipped = file.map{line => (line.split("\t")(0),line.split("\t")(1))}.zipWithIndex.map{case((x,y),v) => {(v,(x,y))}}.cache()
//var xyZipped = xy.
var car = xyZipped.cartesian(xyZipped)
xyZipped.unpersist()
var diagRemoval = car.filter{case((i,(x1,y1)),(j,(x2,y2))) => (i!=j)}.cache()
var similarities = diagRemoval.map{case((i,(x1,y1)),(j,(x2,y2))) => {
      var sum = (x2.toDouble - x1.toDouble)*(x2.toDouble - x1.toDouble) + (y2.toDouble - y1.toDouble)*(y2.toDouble - y1.toDouble)
      var g = 1.0
      (i,j,Math.exp(-g*sum))
      }
      }
val pic = new PowerIterationClustering().setK(3).setMaxIterations(10)
val model = pic.run(similarities)


model.assignments.foreach { a =>
  println(s"${a.id} -> ${a.cluster}")
}

var assignments = model.assignments
var idCluster = assignments.map{case line => (line.id,line.cluster)}
var sortedById = idCluster.sortByKey()

var joined = sortedById.join(xyZipped).map{case(i,(c,(x,y))) => (c,(x+","+y))}.sortByKey()
var joinedx = joined.map{case(i,xy) => (i,xy.split(",")(0).toDouble)}.groupByKey()
var joinedy = joined.map{case(i,xy) => (i,xy.split(",")(1).toDouble)}.groupByKey()
var newJoin = joinedx.join(joinedy)
var arrayed = newJoin.map{case(i,(a,b)) => (i,(a.toArray,b.toArray))}
var cluster0 = arrayed.filter{case(i,(a,b)) => i==0}
var cluster0x = cluster0.flatMap{case(i,(a,b)) => a}.collect().toSeq
var cluster0y = cluster0.flatMap{case(i,(a,b)) => b}.collect().toSeq
var cluster1 = arrayed.filter{case(i,(a,b)) => i==1}
var cluster1x = cluster1.flatMap{case(i,(a,b)) => a}.collect().toSeq
var cluster1y = cluster1.flatMap{case(i,(a,b)) => b}.collect().toSeq
var cluster2 = arrayed.filter{case(i,(a,b)) => i==2}
var cluster2x = cluster2.flatMap{case(i,(a,b)) => a}.collect().toSeq
var cluster2y = cluster2.flatMap{case(i,(a,b)) => b}.collect().toSeq
val f = Figure()
val p = f.subplot(0)
val q = f.subplot(1)
val r = f.subplot(2)
p += plot(cluster0x,cluster0y,'+',"blue")
p += plot(cluster1x,cluster1y,'+',"green")
p += plot(cluster2x,cluster2y,'+',"red")
//println("hi")
//-----------------------------------------------------------------------------------------------
//var xes = joined.map{case(i,xy) => (i,xy.split(",")(0).toDouble)}
//var yes = joined.map{case(i,xy) => (i,xy.split(",")(1).toDouble)}
//p += plot(x, x :^ 2.0,'+',"blue")
//p += plot(x, x :^ 3.0,'.',"red")
p.xlabel = "cluster 0 x axis"
p.ylabel = "cluster 0 y axis"
q.xlabel = "cluster 1 x axis"
q.ylabel = "cluster 1 y axis"
r.xlabel = "cluster 2 x axis"
r.ylabel = "cluster 2 y axis"

f.saveas("pic.png") // save current figure as a .png, eps and pdf also supported

}
}
