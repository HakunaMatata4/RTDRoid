set terminal postscript eps color
set output "Graph.ps"
set title "Latency curve"
set xlabel "No. of Alarms (Pressure)"
set ylabel "Latency(ns)"
plot "latency.data" using 1:2 title "Worst case latency" with linespoints
set term x11
