package org.matsim.ovgu.berlin.input;

public class Input {
	public static void main(String[] args) {
	}

	public static final double serviceTime = 2 * 60;
	public static final double standardTW = 10 * 60;
	public static final double premiumTW = 2 * 60;
	public static final double staticBuffer = 0 * 60;
	public static final String depot = "9826";

	// link id's for example tour
	public static final String[] tour = new String[] { "9826", "10837", "37615", "122985", "34319", "97538", "82306",
			"113960", "76890", "64709", "18863", "14787", "116212", "63691", "30311", "76811", "20545", "142877",
			"118271", "29572" };

	// expected avg traveltimes for example tour
	public static final double[] avgTT = new double[] { 0.0, 1386.78514, 461.0143096, 660.7601614, 482.7117253,
			531.2474148, 615.2137168, 636.927544, 524.2512817, 773.1225155, 800.9296293, 404.1527514, 525.0083581,
			599.2197634, 593.5771799, 760.3694996, 389.5929394, 753.3953592, 605.2273493, 837.7488374 };

	// expected min traveltimes for example tour
	public static final double[] minTT = new double[] { 0.0, 1269.541339, 397.5076803, 625.1344664, 431.6207752,
			490.3279327, 592.9618135, 589.0757879, 469.5942699, 720.6765824, 725.6061951, 379.5941993, 495.9362131,
			545.9396331, 525.6032059, 662.4026367, 345.5011254, 656.0057098, 512.9801903, 743.2175726 };

	// expected max traveltimes for example tour
	public static final double[] maxTT = new double[] { 0.0, 1592.517231, 533.4664708, 738.1203464, 582.9350352,
			582.2497275, 663.5987905, 702.907558, 653.5758981, 896.8119032, 892.120581, 445.7765853, 560.4866599,
			667.6098697, 677.0537879, 839.0182749, 474.0636164, 887.3781677, 750.0190174, 1049.122674 };

	// link id's for a larger example tour
	public static final String[] lTour = new String[] { "9826", "10837", "37615", "122985", "34319", "97538", "82306",
			"113960", "76890", "64709", "18863", "14787", "116212", "63691", "30311", "76811", "20545", "142877",
			"118271", "29572", "9826", "10837", "37615", "122985", "34319", "97538", "82306", "113960", "76890",
			"64709", "18863", "14787", "116212", "63691", "30311", "76811", "20545", "142877", "118271", "29572",
			"9826", "10837", "37615", "122985", "34319", "97538", "82306", "113960", "76890", "64709", "18863", "14787",
			"116212", "63691", "30311", "76811", "20545", "142877", "118271", "29572", "9826", "10837", "37615",
			"122985", "34319", "97538", "82306", "113960", "76890", "64709", "18863", "14787", "116212", "63691",
			"30311", "76811", "20545", "142877", "118271", "29572", "9826", "10837", "37615", "122985", "34319",
			"97538", "82306", "113960", "76890", "64709", "18863", "14787", "116212", "63691", "30311", "76811",
			"20545", "142877", "118271", "29572", "9826", "10837", "37615", "122985", "34319", "97538", "82306",
			"113960", "76890", "64709", "18863", "14787", "116212", "63691", "30311", "76811", "20545", "142877",
			"118271", "29572", "9826", "10837", "37615", "122985", "34319", "97538", "82306", "113960", "76890",
			"64709", "18863", "14787", "116212", "63691", "30311", "76811", "20545", "142877", "118271", "29572" };

	// expected avg traveltimes for larger example tour
	public static final double[] lAvgTT = new double[] { 0, 1386.78514, 461.0143096, 660.7601614, 482.7117253,
			531.2474148, 615.2137168, 636.927544, 524.2512817, 773.1225155, 800.9296293, 404.1527514, 525.0083581,
			599.2197634, 593.5771799, 760.3694996, 389.5929394, 753.3953592, 605.2273493, 837.7488374, 784.4261075,
			1386.78514, 461.0143096, 660.7601614, 482.7117253, 531.2474148, 615.2137168, 636.927544, 524.2512817,
			773.1225155, 800.9296293, 404.1527514, 525.0083581, 599.2197634, 593.5771799, 760.3694996, 389.5929394,
			753.3953592, 605.2273493, 837.7488374, 784.4261075, 1386.78514, 461.0143096, 660.7601614, 482.7117253,
			531.2474148, 615.2137168, 636.927544, 524.2512817, 773.1225155, 800.9296293, 404.1527514, 525.0083581,
			599.2197634, 593.5771799, 760.3694996, 389.5929394, 753.3953592, 605.2273493, 837.7488374, 784.4261075,
			1386.78514, 461.0143096, 660.7601614, 482.7117253, 531.2474148, 615.2137168, 636.927544, 524.2512817,
			773.1225155, 800.9296293, 404.1527514, 525.0083581, 599.2197634, 593.5771799, 760.3694996, 389.5929394,
			753.3953592, 605.2273493, 837.7488374, 784.4261075, 1386.78514, 461.0143096, 660.7601614, 482.7117253,
			531.2474148, 615.2137168, 636.927544, 524.2512817, 773.1225155, 800.9296293, 404.1527514, 525.0083581,
			599.2197634, 593.5771799, 760.3694996, 389.5929394, 753.3953592, 605.2273493, 837.7488374, 784.4261075,
			1386.78514, 461.0143096, 660.7601614, 482.7117253, 531.2474148, 615.2137168, 636.927544, 524.2512817,
			773.1225155, 800.9296293, 404.1527514, 525.0083581, 599.2197634, 593.5771799, 760.3694996, 389.5929394,
			753.3953592, 605.2273493, 837.7488374, 784.4261075, 1386.78514, 461.0143096, 660.7601614, 482.7117253,
			531.2474148, 615.2137168, 636.927544, 524.2512817, 773.1225155, 800.9296293, 404.1527514, 525.0083581,
			599.2197634, 593.5771799, 760.3694996, 389.5929394, 753.3953592, 605.2273493, 837.7488374 };

	// expected min traveltimes for larger example tour
	public static final double[] lMinTT = new double[] { 0, 1269.541339, 397.5076803, 625.1344664, 431.6207752,
			490.3279327, 592.9618135, 589.0757879, 469.5942699, 720.6765824, 725.6061951, 379.5941993, 495.9362131,
			545.9396331, 525.6032059, 662.4026367, 345.5011254, 656.0057098, 512.9801903, 743.2175726, 687.2322049,
			1269.541339, 397.5076803, 625.1344664, 431.6207752, 490.3279327, 592.9618135, 589.0757879, 469.5942699,
			720.6765824, 725.6061951, 379.5941993, 495.9362131, 545.9396331, 525.6032059, 662.4026367, 345.5011254,
			656.0057098, 512.9801903, 743.2175726, 687.2322049, 1269.541339, 397.5076803, 625.1344664, 431.6207752,
			490.3279327, 592.9618135, 589.0757879, 469.5942699, 720.6765824, 725.6061951, 379.5941993, 495.9362131,
			545.9396331, 525.6032059, 662.4026367, 345.5011254, 656.0057098, 512.9801903, 743.2175726, 687.2322049,
			1269.541339, 397.5076803, 625.1344664, 431.6207752, 490.3279327, 592.9618135, 589.0757879, 469.5942699,
			720.6765824, 725.6061951, 379.5941993, 495.9362131, 545.9396331, 525.6032059, 662.4026367, 345.5011254,
			656.0057098, 512.9801903, 743.2175726, 687.2322049, 1269.541339, 397.5076803, 625.1344664, 431.6207752,
			490.3279327, 592.9618135, 589.0757879, 469.5942699, 720.6765824, 725.6061951, 379.5941993, 495.9362131,
			545.9396331, 525.6032059, 662.4026367, 345.5011254, 656.0057098, 512.9801903, 743.2175726, 687.2322049,
			1269.541339, 397.5076803, 625.1344664, 431.6207752, 490.3279327, 592.9618135, 589.0757879, 469.5942699,
			720.6765824, 725.6061951, 379.5941993, 495.9362131, 545.9396331, 525.6032059, 662.4026367, 345.5011254,
			656.0057098, 512.9801903, 743.2175726, 687.2322049, 1269.541339, 397.5076803, 625.1344664, 431.6207752,
			490.3279327, 592.9618135, 589.0757879, 469.5942699, 720.6765824, 725.6061951, 379.5941993, 495.9362131,
			545.9396331, 525.6032059, 662.4026367, 345.5011254, 656.0057098, 512.9801903, 743.2175726 };

	// expected max traveltimes for larger example tour
	public static final double[] lMaxTT = new double[] { 0, 1592.517231, 533.4664708, 738.1203464, 582.9350352,
			582.2497275, 663.5987905, 702.907558, 653.5758981, 896.8119032, 892.120581, 445.7765853, 560.4866599,
			667.6098697, 677.0537879, 839.0182749, 474.0636164, 887.3781677, 750.0190174, 1049.122674, 896.4662972,
			1592.517231, 533.4664708, 738.1203464, 582.9350352, 582.2497275, 663.5987905, 702.907558, 653.5758981,
			896.8119032, 892.120581, 445.7765853, 560.4866599, 667.6098697, 677.0537879, 839.0182749, 474.0636164,
			887.3781677, 750.0190174, 1049.122674, 896.4662972, 1592.517231, 533.4664708, 738.1203464, 582.9350352,
			582.2497275, 663.5987905, 702.907558, 653.5758981, 896.8119032, 892.120581, 445.7765853, 560.4866599,
			667.6098697, 677.0537879, 839.0182749, 474.0636164, 887.3781677, 750.0190174, 1049.122674, 896.4662972,
			1592.517231, 533.4664708, 738.1203464, 582.9350352, 582.2497275, 663.5987905, 702.907558, 653.5758981,
			896.8119032, 892.120581, 445.7765853, 560.4866599, 667.6098697, 677.0537879, 839.0182749, 474.0636164,
			887.3781677, 750.0190174, 1049.122674, 896.4662972, 1592.517231, 533.4664708, 738.1203464, 582.9350352,
			582.2497275, 663.5987905, 702.907558, 653.5758981, 896.8119032, 892.120581, 445.7765853, 560.4866599,
			667.6098697, 677.0537879, 839.0182749, 474.0636164, 887.3781677, 750.0190174, 1049.122674, 896.4662972,
			1592.517231, 533.4664708, 738.1203464, 582.9350352, 582.2497275, 663.5987905, 702.907558, 653.5758981,
			896.8119032, 892.120581, 445.7765853, 560.4866599, 667.6098697, 677.0537879, 839.0182749, 474.0636164,
			887.3781677, 750.0190174, 1049.122674, 896.4662972, 1592.517231, 533.4664708, 738.1203464, 582.9350352,
			582.2497275, 663.5987905, 702.907558, 653.5758981, 896.8119032, 892.120581, 445.7765853, 560.4866599,
			667.6098697, 677.0537879, 839.0182749, 474.0636164, 887.3781677, 750.0190174, 1049.122674 };

	// nodes for example tour
	public static final long[] tourNodes = new long[] { 100163057, 3712222554l, 26870674, 29218295, 29270520, 29785890,
			282395034, 268224213, 4313424156l, 275726428, 1380016717, 677228677, 26754202, 274977654, 29686277,
			26785807, 269843861, 26761185, 26554202, 254870237 };

	// nodes for a larger example tour
	public static final long[] lTourNodes = new long[] { 100163057, 3712222554l, 26870674, 29218295, 29270520, 29785890,
			282395034, 268224213, 4313424156l, 275726428, 1380016717, 677228677, 26754202, 274977654, 29686277,
			26785807, 269843861, 26761185, 26554202, 254870237, 100163057, 3712222554l, 26870674, 29218295, 29270520,
			29785890, 282395034, 268224213, 4313424156l, 275726428, 1380016717, 677228677, 26754202, 274977654,
			29686277, 26785807, 269843861, 26761185, 26554202, 254870237, 100163057, 3712222554l, 26870674, 29218295,
			29270520, 29785890, 282395034, 268224213, 4313424156l, 275726428, 1380016717, 677228677, 26754202,
			274977654, 29686277, 26785807, 269843861, 26761185, 26554202, 254870237, 100163057, 3712222554l, 26870674,
			29218295, 29270520, 29785890, 282395034, 268224213, 4313424156l, 275726428, 1380016717, 677228677, 26754202,
			274977654, 29686277, 26785807, 269843861, 26761185, 26554202, 254870237, 100163057, 3712222554l, 26870674,
			29218295, 29270520, 29785890, 282395034, 268224213, 4313424156l, 275726428, 1380016717, 677228677, 26754202,
			274977654, 29686277, 26785807, 269843861, 26761185, 26554202, 254870237, 100163057, 3712222554l, 26870674,
			29218295, 29270520, 29785890, 282395034, 268224213, 4313424156l, 275726428, 1380016717, 677228677, 26754202,
			274977654, 29686277, 26785807, 269843861, 26761185, 26554202, 254870237, 100163057, 3712222554l, 26870674,
			29218295, 29270520, 29785890, 282395034, 268224213, 4313424156l, 275726428, 1380016717, 677228677, 26754202,
			274977654, 29686277, 26785807, 269843861, 26761185, 26554202, 254870237 };

}