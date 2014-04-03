outwidth=960
outheight=540
outshape=${outwidth}x${outheight}
infile=$1
outfile=$2

boost=1

ffmpeg -i $infile -f rawvideo -pix_fmt rgb24 -s $outshape pipe:1 | colour_fade.py $outshape $boost | ffmpeg -f rawvideo -pix_fmt rgb24 -s $outshape -i pipe:0 -b 3000k -y $outfile

