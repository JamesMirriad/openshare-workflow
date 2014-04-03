outwidth=960
outheight=540
outshape=${outwidth}x${outheight}
infile=$1
outfile=$2

# echo $outwidth, $outheight, $outshape

ffmpeg -i $infile -f rawvideo -pix_fmt rgb24 -s $outshape pipe:1 | burn_bitc.py $outshape | ffmpeg -f rawvideo -pix_fmt rgb24 -s $outshape -i pipe:0 -b 3000k -y $outfile


