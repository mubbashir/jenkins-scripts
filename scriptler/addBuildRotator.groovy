import hudson.tasks.*
def jobs = Hudson.instance.items
def daysRotator = new LogRotator(30, -1, -1, -1) 
def buildsRotator = new LogRotator(-1, 30, -1, -1) 
  println "Builds Rotaror-> numToKeep: "+buildsRotator.numToKeep + 
  	  " daysToKeep: "+buildsRotator.daysToKeep 
  println "daysRotator-> numToKeep: "+daysRotator.numToKeep + 
  	   " daysToKeep: "+daysRotator.daysToKeep
jobs.each {
    println("Job name:" + it.name )
     if(it.getScm() instanceof hudson.plugins.git.GitSCM) {
          println("Branches :" + it.getScm().getBranches().toString() )
          
          if (it.getScm().getBranches().toString().contains("**") ){
            println "Assign: it.logRotator= buildsRotator"
              it.logRotator=buildsRotator
          }
          else{
            println "Assign: it.logRotator= daysRotator"
              it.logRotator=daysRotator
          }
      it.save()
     }
}
