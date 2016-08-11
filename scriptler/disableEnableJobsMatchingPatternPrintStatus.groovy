/*** BEGIN META {
  "name" : "Disable/Enable Jobs Matching Pattern and print states",
  "comment" : "Find all jobs with names matching the given pattern and either disables or enables them, using funtion updateJobStatus() you can also print the status of job using funtion printJobsStatus()",
  "parameters" : [ 'jobPattern', 'disableOrEnable' ],
  "core": "1.409",
  "authors" : [
    { name : "Ahmed Mubbashir Khan" }
  ]
} END META**/

// Pattern to search for. Regular expression.
//def jobPattern = "some-pattern-.*"

// Should we be disabling or enabling jobs? "disable" or "enable", case-insensitive.
//def disableOrEnable = "enable"

def myJobPattern = ~/.*kraken.*|.*greedy.*/
def disableOrEnable = "disable"

println("Jobs to update: " + myJobPattern +" with status: " + disableOrEnable)

printJobsStatus()
updateJobStatus(myJobPattern, disableOrEnable)
printJobsStatus()

/**
Call the Function to print job status
**/
def printJobsStatus()
{
  def enabled_jobs = 0;
  println("==============")
  println("Printing Jobs Status: " + new Date())
  println("==============")
  for(item in Hudson.instance.items) {
    if (item.disabled!=true){
    enabled_jobs++;
    println(item.name + " runs on node with label ->"+ item.getAssignedLabelString())

    }
  }

  println "--------------${new Date()}: Jobs Stats"
  println "Total no of jobs:" + Hudson.instance.items.size()
  println "Total enable jobs:" + enabled_jobs
  println "Total disabled jobs:" + (Hudson.instance.items.size() - enabled_jobs)
}
//Based on the original https://github.com/jenkinsci/jenkins-scripts/blob/master/scriptler/disableEnableJobsMatchingPattern.groovy
/**
Call the Function to print job status
@param jobPattern: Pattern to search for. Regular expression.
@param Should we be disabling or enabling jobs? "disable" or "enable", case-insensitive.
**/
def updateJobStatus(jobPattern, disableOrEnable){
  println("==============")
  println("Updating Jobs with pattren: "+jobPattern+" To Status: "+disableOrEnable+" " + new Date())
  println("==============")

  def jobStatusFlag = disableOrEnable.toLowerCase()
  def matchedJobs = Jenkins.instance.items.findAll { job ->
          job.name =~ /$jobPattern/
   }

   matchedJobs.each { job ->
          if (jobStatusFlag.equals("disable") && job.disabled != true) {
              println "Disabling matching job ${job.name}"
              job.disable()
          } else if (jobStatusFlag.equals("enable") && job.disabled == true) {
              println "Enabling matching job ${job.name}"
              job.enable()
          }
     job.save()
    }
}
