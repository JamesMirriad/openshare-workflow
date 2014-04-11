#!/usr/bin/python

import Image # Python Imaging Library
import ImageDraw
import ImageFont
import sys, string

# Draw a red cross over an image: useful to chack framing is correct
def draw_cross_over(image):
    draw = ImageDraw.Draw(image)
    col = (255, 0, 0)
    draw.line((0, 0) + image.size, fill=col)
    draw.line((0, image.size[1], image.size[0], 0), fill=col)

# Read a frame from stdin, process it, write it out to stdout
def process_frame(n, width, height, font):
    mode, bytes_per_pixel = "RGB", 3
    data = sys.stdin.read(width * height * bytes_per_pixel)
    image = Image.frombuffer(mode, (width, height), data, "raw", mode, 0, 1)

    # Draw a timecode somewhere on the frame
    draw = ImageDraw.Draw(image)
    tc_rate = 25
    timecode = "TCR %02d:%02d:%02d:%02d" % (n/(tc_rate*3600), (n/(tc_rate*60)) % 60, (n/tc_rate) % 60, n % tc_rate)
    tc_width, tc_height = font.getsize(timecode)
    tc_x, tc_y = image.size[0]/2, int(image.size[1] * 0.85)
    draw.rectangle((tc_x - tc_width/2, tc_y-tc_height/2, tc_x+tc_width/2, tc_y+tc_height/2), fill=(0,0,0))
    draw.text((tc_x-tc_width/2, tc_y-tc_height/2), timecode, fill=(255,255,255), font=font)

    del draw 

    sys.stdout.write(image.tostring())

# Main program
width, height = map(int, string.split(sys.argv[1], 'x'))

font = ImageFont.truetype("/usr/share/fonts/truetype/ttf-dejavu/DejaVuSansMono.ttf", height/10)

for n in range(10000):
    process_frame(n, width, height, font)
