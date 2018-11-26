#!/usr/bin/env groovy

/*
Author: James Hippler (HipplerJ)
Organization: LogicMonitor, Inc.
Title: Systems Integration Engineer

Description: Systems Integration Engineer Interview
Completed: Monday, November 26, 2018

Filename: HipplerJ_LogicMonitor.groovy

RETURN JSON EXAMPLE FROM JSONPlaceholder
  {
     "userId": 7,
     "id": 64,
     "title": "et fugit quas eum in in aperiam quod",
     "body": "id velit blanditiis\neum ea voluptatem\nmolestiae sint occaecati est eos perspiciatis\nincidunt a error provident eaque aut aut qui"
   },
*/

// Import JsonSlurper to parse text into meaningful data.
import groovy.json.JsonSlurper

// Establish an empty map (key/value) set to store post frequencies
def num_posts = [:]
// Establish an empty map (key/value) set to store titles for each user
def user_titles = [:]
// Establish an empty map (key/value) set to store post ids for each user
def post_ids = [:]
// Establish an empty list to store userIds that post titles beginning with lowercase 's'
def id_list = []
// Establish a File Object for storing our report
def log_report = new File('HipplerJ_Log_Report.txt')
// Create a variable to store the API URL at JSONPlaceholder
def blog_url = "http://jsonplaceholder.typicode.com/posts"   // Used URL provided in Hackerrank description.
// Retrieve text information from JSONPlaceholder REST Service (POST)
def raw_text = blog_url.toURL().text
// Use JsonSlurper to parse JSON POST Response into list
def post_list = new JsonSlurper().parseText(raw_text)

// Loop through each post in the JSON object parsed by JsonSlurper
for(post in post_list) {
  // If the first character in the title is a lower case 's' (Only lowercase 's' was specified in the instructions.  Ignoring capital 'S')
  if(post.title[0] == 's') {
    // If the userId has NOT already been added to the list. Create a new key/value pair and assign 1
    if(!num_posts.containsKey(post.userId)) {
      num_posts.put(post.userId, 1)                         // Create new key/value pair for current userId and post frequency
      user_titles.put(post.userId, [])                      // Create new key/value pair for current userId and Titles
      user_titles[post.userId].add(post.title)              // Add the current post title to the list for the current userId
      post_ids.put(post.userId, [])                         // Create new key/value pair for the current userId and post ID
      post_ids[post.userId].add(post.id)                    // Add the current post id to the list for the current userId
      id_list.add(post.userId)                              // Add userId to List (May not be needed)
    // Otherwise, increment the frequency counter by 1
    } else {
      num_posts[post.userId] = num_posts[post.userId] + 1   // Increment post frequency for current user
      user_titles[post.userId].add(post.title)              // Add the current post title to the list for the current userId
      post_ids[post.userId].add(post.id)                    // Add the current post id to the list for the current userId
    }
  }
}

// *** I WAS UNABLE TO FIND WHERE A USERNAME WAS PROVIDED IN THE POST OBJECT.  I ONLY FOUND 'USERID', 'ID', 'TITLE', AND 'BODY' ****
// Generate the report both to the console and to a file
println("RESULTS HAVE ALSO BEEN LOGGED TO \'$log_report\' IN THE CURRENT DIRECTORY\n")
log_report.write ""
for(userId in id_list) {
  def length = post_ids[userId].size()                      // Clear file HipplerJ_Log_Report.txt to replace with new report information
  log_report << "***** User ID: $userId *****\n"            // Write the current UserID to the file (HipplerJ_Log_Report.txt)
  println("***** User ID: $userId *****")                   // Print the current UserID
  log_report << "Number of Posts Starting with 's': ${num_posts[userId]}\n" // Write the number of posts starting with 's' for the current userID to the file (HipplerJ_Log_Report.txt)
  println("Number of Posts Starting with 's': ${num_posts[userId]}") // Print the number of posts starting with 's' for the current userID
  for(int i = 0; i < length; i ++) {                        // Loop through each post id associated with the current user
    log_report << "- Post #${i + 1} (ID ${post_ids[userId][i]}): ${user_titles[userId][i]}\n" // Write the title and title id to the file (HipplerJ_Log_Report.txt)
    println("- Post #${i + 1} (ID ${post_ids[userId][i]}): ${user_titles[userId][i]}") // Print the title and the title id
  }
  log_report << "\n"                                        // Write a new line to the file (HipplerJ_Log_Report.txt)
  println()                                                 // Print a new line to maintain spacing
}
