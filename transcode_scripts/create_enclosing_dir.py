#!/usr/bin/python
# create the enclosing directory for the destination file
import sys, os, string

dirname = string.join(string.split(sys.argv[1], "/")[:-1], "/")
try: 
    os.makedirs(dirname) 
except: 
    pass

