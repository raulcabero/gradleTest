package utility

import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.SpecInfo
import org.spockframework.runtime.model.FeatureInfo
import org.spockframework.runtime.model.MethodInfo

class TimingExtension extends AbstractAnnotationDrivenExtension<Time> {
  def timeSpec = false
  def timedFeatures = []

  void visitSpecAnnotation(Time annotation, SpecInfo spec) {
	println("entra visitSpecAnnotation")
    timeSpec = true
  }

  
  
  void visitSpec(SpecInfo spec) {
	println("entra visitSpec")
	spec.addListener(new TimingRunListener(timeSpec, timedFeatures))
	
  }
  
  void visitFeatureAnnotation(Time annotation, FeatureInfo feature) {
	  println("entra visitFeatureAnnotation")
	  timedFeatures << feature.name
	  println(timedFeatures.size)
	}

  
  
}