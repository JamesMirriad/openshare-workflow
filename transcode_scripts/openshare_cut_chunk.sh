outwidth=1920
outheight=1080
outshape=${outwidth}x${outheight}
infile=$1
outfile=$2

# echo $outwidth, $outheight, $outshape

ffmpeg -i $infile -f rawvideo -pix_fmt rgb24 -s $outshape pipe:1 | python copy_range.py $outshape  | python colour_fade.py $outshape 0 | ffmpeg -f rawvideo -pix_fmt rgb24 -s $outshape -i pipe:0 -b 6000k -y $outfile


