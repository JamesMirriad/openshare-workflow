outwidth=960
outheight=540
outshape=${outwidth}x${outheight}
infile=$1
outfile=$2

boost=1

# create the enclosing directory for the destination file
/usr/local/bin/create_enclosing_dir.py $outfile

ffmpeg -i $infile -f rawvideo -pix_fmt rgb24 -s $outshape pipe:1 | /usr/local/bin/colour_fade.py $outshape $boost | ffmpeg -f rawvideo -pix_fmt rgb24 -s $outshape -i pipe:0 -b 3000k -y $outfile

