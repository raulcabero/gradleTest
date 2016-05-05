package utility

import org.spockframework.runtime.AbstractRunListener;
import org.spockframework.runtime.model.*;

class TimingRunListener extends AbstractRunListener {

  def timeSpec = false
  def specStartTime
  def featureStartTimes = [:]

  TimingRunListener(timeSpec, timedFeatures, timedFixtures) {
	  this.timeSpec = timeSpec
	  timedFeatures.each { featureTimes[it] = null }
	}
  
  TimingRunListener(timeSpec, timedFeatures) {
	  this.timeSpec = timeSpec
	  timedFeatures.each {featureStartTimes[it] = null}
	}
  
	private now() {
	  System.currentTimeMillis()
	}
  
	private start(name) {
	  println "starting $name …"
	}
  
	private done(name, started, finished = now()) {
	  println "$name took ${finished - started} milliseconds"
	}


@Override
public void beforeSpec(SpecInfo spec) {
	if (timeSpec) {
      specStartTime = now()
      start("spec '$spec.name'")
    }
}


@Override
public void beforeFeature(FeatureInfo feature) {
	println("before Feacture" + feature)
	if (featureStartTimes.containsKey(feature.name)) {
	  println("contains in beforeFeacture")
      featureStartTimes[feature.name] = now()
      start("feature '$feature.name'")
    }
}


@Override
public void afterFeature(FeatureInfo feature) {
	def startedAt = featureStartTimes[feature.name]
    if (startedAt) {
      done("afterFeature feature '$feature.name'" , startedAt)
    }
}


@Override
public void afterSpec(SpecInfo spec) {
	if (specStartTime) {
      done("afterspec '$spec.name'" , specStartTime)
    }
}
  
}
