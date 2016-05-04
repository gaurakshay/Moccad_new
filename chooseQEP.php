<?php
	class randomEstimation //Container object for the random estimations.
	{
		public $objTime;
		public $objMoney;
		public $objPower;
		public $score;

		function estimationToString()
		{
			return "(T=".$this->objTime." seconds, M=$".$this->objMoney.", P=".$this->objPower." mAh) -> ".$this->score;
		}
	}

	function chooseQEP($estimationStack, $time, $money, $power)
	{
		$size = count($estimationStack);

		$bestViolation = null;
		$best = null;

		for($y = 0; $y < $size; $y++)
		{
			$est = $estimationStack[$y];
			$violate = false;
			if($est->objTime > $time)
			{
				$violate = true;
			}
			if($est->objMoney > $money)
			{
				$violate = true;
			}
			if($est->objPower > $power)
			{
				$violate = true;
			}

			if($violate == true)
			{
				if($bestViolation != null)
				{
					if($est->score < $bestViolation->score)
						$bestViolation = $est;
				}
				else
				{
					if($est->score < 1) //1 is the devault score
						$bestViolation = $est;
				}
			}
			else
			{
				if($best != null)
				{
					if($est->score < $best->score)
						$best = $est;
				}
				else
				{
					if($est->score < 1) //1 is the devault score
						$best = $est;
				}
			}
		}

		$chosenEstimation = new randomEstimation;
		if($best == null)
		{
			$chosenEstimation = $bestViolation;
		}
		else
		{
			$chosenEstimation = $best;
		}

		return $chosenEstimation;
	}

	function generateRandomEstimations($size, $time, $money, $power)
	{
		$logFile = fopen("MOCCAD_LOG.txt", "a"); //Log file
		$estimationStack = array();
		for($x = 0; $x < $size; $x++)
		{
			$estimation = new randomEstimation;
			$estimation->objMoney = mt_rand(0, 10) * .01; //$0.00 - $0.10
			$estimation->objTime = mt_rand(0, 10); //0 - 10 seconds
			$estimation->objPower = mt_rand(0, 5) * .1; //0 - 0.5 mAh
			$estimation->score = ($time * ($estimation->objTime / 10)) + ($money * ($estimation->objMoney / .10))
				+ ($power * ($estimation->objPower / .5));

			fwrite($logFile, $estimation->{'estimationToString'}()."\n");
	
			array_push($estimationStack, $estimation);
		}
		return $estimationStack;
	}
?>