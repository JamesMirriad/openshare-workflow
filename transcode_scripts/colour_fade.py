#!/usr/bin/python

import Image # Python Imaging Library
import ImageDraw
import ImageFont
import scipy, scipy.misc
import sys, string

# Read a frame from stdin, process it, write it out to stdout
def process_frame(n, width, height, boost):
    mode, bytes_per_pixel = "RGB", 3
    data = sys.stdin.read(width * height * bytes_per_pixel)
    image = Image.frombuffer(mode, (width, height), data, "raw", mode, 0, 1)

    # Mute and desaturate
    array = scipy.misc.fromimage(image)
    if boost:
        array[:,:,:] = ((array[:,:,:] - 0.1)/0.9)**(1/0.9)
    else:
        array[:,:,:] = 0.1 + 0.9 * array[:,:,:]**0.9
    image = scipy.misc.toimage(array, 255, 0)
    sys.stdout.write(image.tostring())

# Main program
width, height = map(int, string.split(sys.argv[1], 'x'))
boost = int(sys.argv[2])

# An hour, but not forever
for n in range(60*60*25):
    process_frame(n, width, height, boost)
