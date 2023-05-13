# powersim
Powersim is a bottom-up power demand simulator that generates traces for household activities.

It uses a postgresql database to store all the simulated data. 

The system has two components, the simulator, and the analyser.

## Simulator

The simulator generates the load traces for a number of households. 
Some devices are considered inflexible, and thus have to consume electricity on demand, while others, like dishwashers or car charging, are considered flexible demand, and can be shifted to improve the aggregate load profile across households.

The simulator also has a number of control policies implemented. While the default on-demand policy simulates consumption without any demand control, there is also a `nightshift` policy which postpones 
flexible demand to the night, when (presumably) the aggregate load is lower, and an EWMA policy, where demand is shifted according to the current trend, and flexible demand is scheduled to fill in during periods of 
low demand.

## Analyser 

A web-based tool which shows the aggregate load profile, with capabilities of drilling down into the individual households and see their power consumption, categorised by device.

The algorithms used by the simulator are described in:

Bajada, Josef, Maria Fox, and Derek Long. "Load modelling and simulation of household electricity consumption for the evaluation of demand-side management strategies." IEEE PES ISGT Europe 2013. IEEE, 2013.

If you find this useful for your work, kindly cite it as follows:

```
@inproceedings{bajada2013load,
  title={Load modelling and simulation of household electricity consumption for the evaluation of demand-side management strategies},
  author={Bajada, Josef and Fox, Maria and Long, Derek},
  booktitle={IEEE PES ISGT Europe 2013},
  pages={1--5},
  year={2013},
  organization={IEEE}
}
```

**Note: This repository hasn't been updated for quite a while.** 
