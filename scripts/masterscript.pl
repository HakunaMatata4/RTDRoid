#!/usr/bin/perl

use strict;
use warnings;

my $tempFile="tempFile.tmp";
my $consolidatedFile="consolidatedFile.tmp";
my $latencyFile="latencyFile.tmp";
my $outfile="./latency.data";

print `cat *.time > $tempFile`;
print `cat *.time > $tempFile`;
print `sed -i "s/Timestamp.*//g" $tempFile`;
print `sed -i "s/Event.*//g" $tempFile`;
print `sed -i "s/.*://g" $tempFile`;
print `cat $tempFile |tr '\n' ' ' > $consolidatedFile`;
`rm -rf $tempFile`;
print `sed -i "s/Thread/\\n/g" $consolidatedFile`;
print `sed -i "s/_/ /g" $consolidatedFile`;
print `sed -i "s/\.time/ /g" $consolidatedFile`;
print `sed -i "s/ \\+/ /g" $consolidatedFile`;
print `sed -i '/^ /d' $consolidatedFile`;

`cp $consolidatedFile $tempFile`;
`sort -n $tempFile > $consolidatedFile`;

print `awk '{ print \$1" "\$2" "\$3" "\$5-\$4; }' $consolidatedFile > $latencyFile`;
####################################################################################
my @data=();
open IN, "<$latencyFile" or die "File not found\n$!";
my $i=1;
my $high=0;
while(<IN>)
{
	chomp($_);
	my @line=split/ /,$_;
	my $key=$line[0];
	my $localhigh=$line[3];
	chomp($key);
	if($key != $i)
	{
		my $values=$i." ".$high;
		push @data , $values;
		$i++;
		$high=$localhigh;
	}
	else
	{		
		if($high <$localhigh)
		{
			$high=$localhigh;
		}
	}
}
push @data , $i." ".$high;
close IN;
open(my $fh, '>', $outfile) or die "Could not open file '$outfile' $!";
foreach my $arr(@data)
{
	print $fh $arr."\n";
}
close $fh;
print "Data file successfully written!\n";
##############################################################################
print `gnuplot ./plot.gp`;
print "Graphs generated successfully as Graph.ps!\n";
