package org.matsim.ovgu.berlin;

import org.matsim.ovgu.berlin.createNetworkChangeEvents.CreateNetworkChangeEventsFile;
import org.matsim.ovgu.berlin.createTravelTimeMatrix.Version1_ReadChangeEvents;
import org.matsim.ovgu.berlin.createTravelTimeMatrix.Version2_RunSimReadSim;
import org.matsim.ovgu.berlin.createTravelTimeMatrix.Version3_RunSimReadEvents;
import org.matsim.ovgu.berlin.evaluation.RunEvalution;

public class Run {

	public static void main(String[] args) {
		
//TODO: readChangeEvents produces different results than runSimReadEvents -> so it's not used

// create network change events
		// setup values in class "CreateNetworkChangeEventsFile"
//    	CreateNetworkChangeEventsFile.run(); // not checked

// create travel time matrix for tour
		// setup values in class "Input"
//    	Version1_ReadChangeEvents.run(InputTour.tour); // not implemented
//    	Version2_RunSimReadSim.run(InputTour.tour);
//    	Version3_RunSimReadEvents.run(InputTour.tour);

		
				
// run evaluation
// create travel time Matrix included (Version3_RunSimReadEvents)
		
		// setup values in class "Input"
		RunEvalution eva = new RunEvalution();
		
//		String name = args[0];
//      int from = Integer.parseInt(args[1]);
//      int to = Integer.parseInt(args[2]);
//      boolean runBuffers = Boolean.parseBoolean(args[3]);
//      String windowMethod = args[4];
//      boolean runEvaluation = Boolean.parseBoolean(args[5]);
//		boolean runSummary = Boolean.parseBoolean(args[6]);
//		boolean ttWithSim = Boolean.parseBoolean(args[7]);
//		boolean bufWithModel = Boolean.parseBoolean(args[8]);
//		boolean evaluationWithSim = Boolean.parseBoolean(args[9]);

		String[] linkIDs = Run.getMySample2000Links();
		String name = "NextGen";
		int from = 1;
		int to = 1; // from + 99
		boolean runBuffers = true;
		String windowMethod = "PlusMinusArrival";
		boolean runEvaluation = true;
		boolean runSummary = false;
		boolean ttWithSim = false;
		boolean bufWithModel = false;
		boolean evaluationWithSim = true;
		
		eva.run(linkIDs, name, from, to, runBuffers, windowMethod, runEvaluation, runSummary, ttWithSim, bufWithModel,
				evaluationWithSim);
	}

	private static String[] getMySample2000Links() {

		String[] mySample = new String[] { "82713", "74806", "54316", "156435", "75779", "79014", "104183", "25460",
				"40858", "120900", "90407", "108348", "96451", "33708", "82567", "94364", "88679", "125969", "125453",
				"92143", "122466", "67454", "77413", "130949", "125537", "30384", "33699", "141842", "79621", "102518",
				"96178", "134477", "11089", "128544", "22905", "133489", "72822", "111998", "75517", "65154", "154453",
				"18092", "98270", "126378", "59768", "141153", "2441", "101260", "47635", "43049", "76128", "156590",
				"101556", "91085", "27390", "55634", "151454", "121530", "36827", "88000", "137819", "157083", "159231",
				"89775", "75049", "119137", "111652", "93288", "84944", "126725", "136269", "144704", "15905", "80237",
				"22014", "137092", "61286", "90736", "36041", "140744", "29311", "64858", "108839", "21804", "83422",
				"70620", "72808", "148018", "156646", "87081", "38978", "41807", "154246", "111156", "121781", "5984",
				"20693", "98509", "112721", "146330", "133999", "67220", "5119", "43417", "109572", "154503", "126718",
				"34116", "159241", "92237", "102003", "16342", "5813", "100724", "15321", "6137", "8079", "122664",
				"37514", "36006", "87868", "43656", "68169", "8604", "2053", "121878", "105855", "1111", "123578",
				"145379", "126046", "77908", "759", "3766", "15463", "34438", "144900", "125422", "112999", "110141",
				"27287", "69450", "44394", "43271", "122799", "16092", "26964", "78976", "121659", "41503", "24910",
				"34974", "144251", "109520", "77914", "106117", "102797", "61189", "9304", "37842", "7285", "44832",
				"8255", "67212", "53825", "78315", "58595", "11518", "113136", "142198", "5198", "835", "36898",
				"135069", "133237", "24180", "53089", "92348", "10646", "49618", "51970", "26830", "25500", "10284",
				"155023", "18922", "45332", "10635", "28284", "38306", "27814", "43322", "147965", "6865", "22856",
				"153730", "92051", "58728", "22141", "39919", "51962", "29511", "127039", "99863", "121939", "17966",
				"123915", "122292", "41604", "153489", "36147", "160522", "103614", "37317", "62366", "118999",
				"140816", "125138", "52345", "147154", "64652", "126393", "87986", "125302", "35910", "31377", "129297",
				"158681", "92945", "261", "125572", "84735", "61638", "34203", "54277", "118615", "134637", "59305",
				"117897", "5633", "121445", "59357", "9489", "130315", "11443", "329", "43802", "119440", "17439",
				"154019", "12970", "75209", "17746", "17132", "119498", "24754", "121332", "81438", "21727", "122959",
				"34945", "146335", "84570", "104152", "72857", "15995", "131997", "155153", "158254", "98591", "12817",
				"118962", "18967", "144250", "160844", "99513", "158877", "82611", "27990", "52090", "59700", "16397",
				"27302", "157016", "28169", "103414", "70797", "31376", "34062", "37782", "157623", "62635", "24933",
				"92944", "16388", "27353", "67224", "125917", "81647", "146198", "33463", "19576", "23360", "109903",
				"22213", "17315", "3114", "83519", "64857", "37611", "51684", "139665", "141446", "137648", "53396",
				"43203", "126833", "146060", "52255", "104354", "154758", "49117", "37025", "61331", "124481", "35372",
				"52585", "125406", "46062", "51700", "30201", "29968", "123392", "55607", "59405", "26867", "147771",
				"100182", "83777", "89653", "53937", "96104", "52249", "78403", "18993", "102316", "53938", "158200",
				"14573", "65169", "152308", "35752", "73757", "141712", "86132", "148488", "80812", "159699", "41787",
				"32757", "91626", "99859", "52869", "121921", "67569", "2548", "105844", "34037", "79088", "105396",
				"10467", "95969", "27371", "85783", "17271", "115583", "58124", "151396", "37267", "1750", "45404",
				"30788", "95494", "11924", "160591", "1154", "137083", "131158", "29327", "30875", "59797", "88567",
				"20707", "86963", "15714", "37509", "51173", "29522", "24757", "31563", "19583", "71551", "146271",
				"135948", "45135", "9486", "92819", "126881", "103905", "29531", "156984", "71542", "138762", "108118",
				"129429", "62504", "62388", "155903", "52918", "45602", "26742", "55722", "42655", "110733", "92442",
				"21058", "109485", "49613", "38277", "103319", "3888", "53859", "134017", "52830", "13189", "55946",
				"150773", "76560", "156442", "150043", "64114", "142568", "18046", "5989", "90029", "142795", "131613",
				"78115", "149507", "61173", "54190", "230", "156250", "101624", "66686", "42325", "127048", "156206",
				"138933", "68679", "66253", "122000", "77435", "47112", "48259", "123023", "43214", "8814", "137096",
				"156203", "81639", "31213", "141740", "138776", "50814", "987", "36038", "99980", "78920", "137512",
				"58599", "128529", "86089", "137277", "137780", "143746", "82613", "131995", "35414", "62564", "156652",
				"114070", "5986", "72880", "47010", "19977", "1199", "53814", "6266", "30253", "82262", "37302",
				"73166", "91482", "39222", "151222", "86298", "97911", "100135", "87047", "141593", "34213", "147968",
				"35283", "98781", "22925", "80504", "62094", "14278", "144005", "103526", "39210", "111543", "111540",
				"7310", "11057", "16305", "148297", "77935", "111199", "148188", "101579", "86166", "128745", "104243",
				"144160", "66145", "137818", "32484", "85754", "21717", "123822", "84769", "143490", "66724", "66020",
				"139385", "18535", "61919", "49709", "84932", "132966", "100130", "33551", "30437", "121362", "27464",
				"12686", "47516", "145804", "99816", "4254", "90183", "121453", "142817", "55348", "123840", "139547",
				"18563", "84922", "152307", "104827", "112914", "57672", "50976", "79371", "76307", "154588", "85300",
				"100178", "108861", "32684", "41899", "60700", "66212", "41614", "117368", "33974", "155490", "112978",
				"107648", "40246", "89809", "53860", "28430", "34785", "65391", "5122", "5113", "104821", "138424",
				"154726", "38013", "44835", "72057", "150587", "67860", "126128", "97252", "121220", "153002", "49320",
				"35217", "125867", "44173", "140699", "3733", "52553", "90500", "103182", "125326", "140659", "134394",
				"89988", "66056", "6140", "11332", "49708", "12295", "72500", "25523", "154273", "119877", "60493",
				"15974", "155574", "60720", "92130", "8205", "63245", "152208", "102572", "61304", "12004", "83170",
				"14273", "56006", "120731", "95307", "74658", "84839", "63580", "34656", "118139", "44299", "74759",
				"30042", "7726", "133857", "81267", "118949", "108911", "152025", "6995", "9630", "116262", "97586",
				"112736", "96172", "58817", "11081", "117369", "147698", "112350", "74315", "28198", "28627", "63411",
				"103220", "94234", "156370", "12091", "154670", "12545", "159662", "33602", "12035", "27783", "87652",
				"128555", "72432", "141147", "20211", "87199", "154473", "118493", "66127", "42181", "20057", "65046",
				"97989", "90974", "15822", "112105", "104825", "126408", "139824", "57062", "125677", "8106", "136267",
				"33491", "22989", "67610", "30683", "63314", "125542", "33979", "88096", "86053", "106394", "91803",
				"45924", "18402", "103429", "147497", "27017", "156560", "96726", "153474", "150390", "44718", "65281",
				"59979", "153411", "126108", "13915", "43546", "154035", "9394", "103517", "154189", "139254", "2873",
				"49621", "3596", "117104", "147159", "24758", "148215", "112591", "101583", "50530", "143107", "117411",
				"126891", "108503", "24918", "6836", "16790", "79067", "78426", "81272", "85272", "94011", "22756",
				"73256", "122505", "96326", "126160", "110396", "46548", "34596", "144503", "124662", "2174", "59529",
				"41698", "156440", "30759", "18568", "37168", "11874", "126401", "63681", "41508", "117511", "87592",
				"149196", "150095", "49301", "16247", "41439", "72425", "77594", "102175", "86177", "127603", "47518",
				"151997", "41744", "99643", "9505", "16153", "57252", "100723", "87952", "24697", "135920", "14791",
				"83455", "19117", "108123", "65008", "61354", "65252", "99600", "103615", "99178", "19349", "93458",
				"73643", "17541", "74535", "113957", "129980", "109139", "129889", "33311", "48741", "92595", "43666",
				"142517", "25474", "82776", "113553", "126296", "13", "33260", "132422", "115580", "24633", "30464",
				"53921", "11761", "51300", "47520", "40575", "35124", "117312", "40288", "59713", "16101", "28847",
				"70066", "53207", "103248", "139945", "81535", "47349", "113322", "86888", "120492", "66719", "73511",
				"64681", "141073", "112024", "137202", "123921", "28123", "138981", "68623", "61750", "88668", "115445",
				"1331", "65876", "58916", "26996", "84863", "47336", "5881", "9212", "34375", "35665", "25203", "41286",
				"43708", "79968", "156216", "128374", "30675", "84020", "160680", "139574", "159966", "122254",
				"132380", "49751", "118067", "64003", "51961", "153845", "151236", "18704", "139321", "23133", "15010",
				"124867", "85751", "67131", "156343", "155011", "15468", "106016", "35418", "126810", "147153", "76816",
				"123820", "118290", "135885", "14704", "159264", "131694", "79486", "96582", "153395", "6825", "143098",
				"90440", "75513", "102698", "145478", "102947", "30513", "61422", "37143", "8239", "8758", "112898",
				"50552", "64530", "153226", "23937", "136677", "27397", "34039", "45915", "15400", "100192", "74724",
				"60617", "72659", "117315", "84399", "68976", "40324", "160508", "154456", "155227", "147487", "92455",
				"95584", "76350", "97141", "15655", "96764", "82101", "37152", "120427", "127287", "156324", "18238",
				"65298", "61912", "99820", "84446", "22576", "46774", "14442", "84451", "100966", "72048", "121993",
				"136915", "124221", "129786", "53469", "152807", "45801", "17933", "133632", "8060", "34901", "132441",
				"148434", "88356", "119847", "85534", "90922", "50558", "52828", "26831", "22189", "59754", "144156",
				"2139", "32507", "70755", "42292", "93585", "62810", "53534", "124889", "65262", "61453", "7125",
				"20722", "89180", "23328", "57151", "142439", "78645", "27032", "82306", "157672", "8532", "102170",
				"53459", "11275", "138628", "106601", "120738", "78638", "101139", "153864", "28199", "61105", "19804",
				"58623", "159510", "138514", "21974", "22094", "127133", "26864", "129430", "82396", "120877", "141863",
				"15966", "12163", "138894", "45580", "42782", "66294", "74966", "122770", "77909", "139754", "39521",
				"65143", "52967", "35970", "52992", "141177", "92117", "125369", "7828", "18857", "2713", "1751",
				"56683", "45457", "51703", "23440", "125481", "61186", "115584", "42627", "66166", "38870", "82827",
				"145735", "130267", "83769", "26118", "130549", "28275", "5811", "47638", "122593", "53139", "136375",
				"41029", "119438", "43568", "131215", "24111", "38383", "88686", "30662", "52745", "140517", "104259",
				"74821", "89327", "42698", "146344", "86143", "36713", "22785", "48340", "111923", "90322", "61649",
				"137120", "62851", "102059", "143336", "39960", "15310", "92285", "11344", "30120", "71564", "147115",
				"28525", "39946", "62175", "41395", "125925", "20516", "110552", "125916", "141292", "19058", "74400",
				"47471", "98608", "10911", "68331", "149611", "142928", "28548", "142524", "129291", "92141", "69562",
				"91856", "5839", "35706", "109318", "126406", "19133", "39861", "53797", "66940", "41996", "86887",
				"150250", "35810", "19617", "44511", "35413", "104200", "133540", "133136", "1324", "137888", "7659",
				"118502", "76127", "128193", "59277", "145229", "39641", "25655", "80319", "5049", "71632", "136232",
				"11427", "96332", "149412", "77942", "117506", "28141", "140644", "28272", "126678", "90017", "34601",
				"101228", "138037", "80496", "86230", "20813", "144145", "61303", "14270", "24529", "51734", "50586",
				"19599", "130321", "26041", "69267", "3301", "15459", "122238", "134463", "129258", "153367", "138147",
				"100980", "92025", "34042", "88036", "63601", "83184", "143165", "51778", "92074", "138010", "15193",
				"28626", "24188", "22009", "74764", "149315", "50699", "100232", "134271", "37406", "138254", "45755",
				"156243", "81546", "55161", "21519", "127476", "63251", "49550", "14690", "18635", "68153", "67915",
				"97139", "122295", "90426", "99772", "20552", "45705", "109830", "61269", "2458", "20303", "134647",
				"13193", "141974", "110506", "60422", "9428", "43730", "1280", "152709", "121528", "75754", "89201",
				"82505", "160590", "34763", "24372", "112082", "67542", "44793", "147616", "49625", "118497", "62225",
				"8227", "150181", "90854", "155130", "1994", "122437", "117222", "72062", "125985", "86235", "4966",
				"153510", "45336", "19199", "49422", "74961", "53736", "52593", "41756", "63814", "96527", "17324",
				"61117", "55783", "67534", "151904", "5178", "81229", "41173", "16248", "122207", "77703", "133079",
				"74652", "16668", "100094", "133555", "144004", "52360", "33159", "25005", "67232", "111694", "74965",
				"17781", "17917", "125966", "43281", "108180", "38750", "59767", "136950", "124266", "123492", "128736",
				"15227", "27770", "116903", "92817", "137339", "45841", "154263", "87077", "68879", "24899", "52880",
				"98603", "138980", "89396", "77919", "132570", "20556", "79362", "137922", "70752", "57654", "146811",
				"61632", "96082", "119211", "132432", "93603", "108888", "72850", "47846", "131109", "20474", "91320",
				"154922", "122825", "92175", "127478", "112029", "9922", "4964", "136885", "135882", "114752", "1329",
				"18539", "13200", "125081", "17037", "61201", "143372", "76578", "121482", "149929", "33441", "33151",
				"130768", "138571", "11258", "149698", "129247", "129885", "102673", "127676", "22127", "149189",
				"65259", "149431", "111853", "44783", "31173", "146317", "122100", "128162", "99655", "3257", "43198",
				"3485", "104199", "70250", "3101", "616", "114557", "33964", "29548", "22186", "120006", "123944",
				"133813", "35677", "99886", "132369", "94085", "56341", "123407", "123995", "107753", "98091", "86437",
				"16116", "24178", "154095", "50113", "47731", "113622", "38647", "47647", "51647", "92189", "2932",
				"120567", "96645", "77843", "36652", "44292", "33556", "7112", "20066", "8161", "52608", "19449",
				"67319", "7298", "38761", "40395", "24879", "74395", "63671", "122569", "88349", "160663", "78370",
				"123159", "86885", "9927", "82523", "123152", "129715", "101789", "20734", "157714", "134269", "137593",
				"140968", "136694", "14789", "19536", "84689", "29687", "73086", "153190", "77243", "73314", "150195",
				"156134", "77443", "92470", "124051", "86300", "136529", "147500", "87103", "32503", "249", "35608",
				"53726", "63008", "137147", "38397", "137160", "122621", "105596", "26268", "89241", "5638", "119762",
				"58862", "19444", "142114", "34046", "89644", "154971", "139919", "23883", "68528", "138769", "35352",
				"34931", "87809", "74783", "94438", "51054", "136963", "103830", "107224", "132641", "31165", "79066",
				"66299", "114481", "11623", "154777", "116575", "123026", "115413", "119521", "72743", "18781", "23289",
				"129850", "62727", "154434", "35472", "86268", "126888", "85386", "104162", "33855", "116173", "141965",
				"141229", "2904", "104630", "66194", "108138", "104168", "136852", "85770", "7353", "6989", "27472",
				"153833", "12349", "147441", "157631", "22474", "49307", "11692", "27190", "5918", "34449", "125538",
				"92410", "144496", "20033", "159244", "143109", "137527", "101539", "83327", "25679", "117862", "40332",
				"129176", "111878", "115589", "134950", "156260", "147132", "21716", "75647", "24563", "39282",
				"142872", "96328", "29601", "14750", "33704", "34344", "61625", "137997", "122612", "139974", "152289",
				"92342", "48209", "54229", "134694", "95891", "22743", "20357", "28412", "13370", "138008", "21062",
				"64575", "840", "146570", "86724", "133239", "138815", "144541", "81423", "55320", "40245", "111562",
				"54710", "157934", "76579", "140294", "39479", "115431", "93935", "144178", "136433", "86857", "115453",
				"128096", "54160", "137963", "85899", "10265", "56336", "111623", "30230", "48187", "11932", "82949",
				"69522", "24650", "64654", "58532", "148468", "54439", "17633", "92326", "49319", "74644", "124793",
				"17417", "94404", "130771", "86659", "140980", "30234", "139401", "7128", "145174", "83895", "120439",
				"78000", "111125", "46756", "126030", "45274", "51087", "21959", "95731", "39720", "91056", "156286",
				"154715", "24683", "51646", "118285", "47696", "54095", "54212", "74753", "24326", "45736", "122597",
				"87660", "64884", "96933", "5867", "133786", "138011", "155154", "89109", "151256", "119220", "150348",
				"99989", "128875", "58904", "20998", "34092", "454", "64297", "9395", "24731", "31277", "72362",
				"108433", "125763", "111600", "123855", "89803", "57643", "34383", "32309", "99247", "43704", "99601",
				"27883", "6429", "77388", "85362", "3728", "34382", "15795", "159265", "51416", "140410", "112724",
				"150370", "122267", "154287", "157697", "70724", "158867", "122191", "16823", "27373", "25654",
				"122252", "138371", "120967", "20507", "16956", "22190", "139702", "93847", "76900", "112632", "146043",
				"112916", "129208", "110892", "91807", "88354", "159263", "116001", "40289", "124017", "79097", "72890",
				"74666", "47365", "41742", "10418", "153852", "96224", "86278", "127132", "24419", "122554", "97809",
				"86540", "127604", "86017", "41150", "83130", "135436", "157415", "52000", "24720", "3007", "7296",
				"148240", "160104", "123239", "128611", "140272", "23711", "31354", "18020", "83439", "100839",
				"113046", "35383", "40945", "121412", "23933", "91353", "16071", "87968", "83001", "7484", "72400",
				"38339", "40862", "145212", "129836", "1157", "100155", "95073", "11866", "28248", "72446", "76906",
				"143446", "98929", "91238", "101147", "28816", "80035", "24191", "107980", "97883", "56089", "72271",
				"96318", "147370", "24743", "102062", "13353", "76572", "38786", "88110", "91831", "147325", "99891",
				"76567", "154941", "140387", "137872", "72281", "87309", "13525", "147018", "54736", "116169", "18880",
				"99593", "84592", "130434", "120723", "111298", "74491", "84388", "10125", "121461", "109731", "31158",
				"52968", "133138", "140966", "128728", "45302", "146791", "140792", "39537", "37432", "50529", "46987",
				"46772", "67023", "61270", "136680", "98649", "638", "79614", "1281", "148014", "18197", "62208",
				"39500", "17639", "111653", "65955", "47564", "16372", "75653", "79412", "120645", "82587", "126737",
				"92177", "55622", "131059", "26879", "154303", "142289", "99181", "83194", "47333", "140698", "72811",
				"132009", "31293", "103934", "88985", "11935", "138601", "18158", "142486", "55615", "160278", "72812",
				"40953", "79884", "42454", "81478", "126107", "27067", "134671", "115155", "77949", "86297", "130453",
				"51395", "67514", "19432", "152335", "3017", "74545", "8748", "149212", "120513", "121387", "146449",
				"78062", "101205", "55418", "157976", "147372", "53475", "110432", "118271", "86762", "89848", "143086",
				"108148", "112119", "109410", "134346", "83178", "144449", "82738", "48226", "31299", "92205", "37416",
				"104700", "11998", "44121", "36145", "93582", "41894", "29268", "6831", "113267", "96122", "62100",
				"43362", "100722", "12533", "93060", "122548", "116223", "159842", "6833", "66739", "37510", "151175",
				"117169", "139418", "27484", "16945", "59482", "18625" };

		return mySample;
	}
	
	private static String[] getMySample200Links() {

		String[] mySample = new String[] { "50878", "55084", "89524", "90752", "11821", "5712", "23654", "23362",
				"84301", "57169", "122136", "153188", "46802", "143895", "63251", "107638", "98217", "58957", "150919",
				"65599", "103731", "20939", "1741", "51709", "138008", "147898", "154926", "27492", "40855", "38336",
				"44485", "105745", "78324", "51923", "9395", "52901", "16260", "28917", "122959", "69223", "28303",
				"41691", "69174", "141348", "88213", "119137", "4555", "105295", "62370", "67134", "93926", "8198",
				"104456", "70921", "110431", "2548", "72827", "74694", "125775", "62546", "15313", "155933", "26651",
				"79044", "151492", "125443", "135571", "16282", "27603", "111609", "122600", "69522", "61226", "30266",
				"53049", "134169", "98363", "139651", "138049", "72854", "141107", "77687", "134841", "114198", "37396",
				"13190", "118184", "108906", "95615", "156215", "27101", "30287", "87308", "149729", "112090", "2920",
				"31344", "96660", "78576", "21451", "115832", "16245", "94555", "72257", "35071", "138131", "92188",
				"86314", "35983", "26293", "29600", "39710", "120422", "59274", "141749", "110533", "136963", "48103",
				"41807", "143092", "101995", "64432", "26742", "113120", "158788", "53448", "97971", "61270", "6356",
				"63395", "66389", "109480", "83200", "72872", "27353", "133824", "15315", "18628", "67864", "146739",
				"36963", "42704", "135068", "54286", "91320", "112977", "44782", "7726", "94556", "16328", "99072",
				"14783", "99543", "113808", "137683", "60657", "154750", "64309", "113825", "122780", "128978",
				"111543", "86610", "110755", "137202", "88285", "134487", "6525", "147406", "125664", "140234",
				"154269", "8745", "77664", "18953", "138525", "156322", "25304", "33670", "18680", "12254", "141143",
				"74395", "31245", "100050", "118139", "158220", "66043", "4785", "18780", "101079", "111833", "304",
				"17132", "114428", "138457", "156557", "28199", "28803", "765" };

		return mySample;
	}
}
