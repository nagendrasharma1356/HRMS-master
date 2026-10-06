#!/bin/bash

# Save the current directory
current_directory=$(pwd)

find "$current_directory" -type d -name "target*" -exec rm -rf {} \;

# Specify the log file
log_file="$current_directory/build_log.txt"

# Check if the skip_tests parameter is provided
skip_tests=false
if [ "$1" == "--skip-tests" ]; then
  skip_tests=true
fi

# Find all subdirectories and run mvn build in each, logging output to the file
for dir in */; do
  if [ -d "$dir" ]; then
    echo "Building in $dir..."
    cd "$dir" || exit

    # Check if skip_tests is true, and skip tests accordingly
    if [ "$skip_tests" == true ]; then
      echo "Skipping tests in $dir based on parameter."
      mvn clean install -DskipTests >> "$log_file" 2>&1
    else
      mvn clean install >> "$log_file" 2>&1
    fi

    build_status=$?
    cd "$current_directory" || exit

    if [ $build_status -ne 0 ]; then
      echo "Build failed in $dir. Check $log_file for details."
    else
      echo "Build completed in $dir"
    fi
  fi
done

echo "All builds completed. Build logs are saved in $log_file."
