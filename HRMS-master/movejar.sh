#!/bin/bash

# Ensure the jars directory exists in the current directory and use it as the destination
DEST_DIR="$(pwd)/jars"
mkdir -p "$DEST_DIR"

# Find and copy all jar files from target directories of subdirectories
find . -type f -path "*/target/*.jar" -exec cp {} "$DEST_DIR" \;

echo "Jar files have been copied to the jars directory"