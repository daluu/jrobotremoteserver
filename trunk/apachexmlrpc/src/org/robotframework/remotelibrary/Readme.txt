Rather than duplicate copies of source code in multiple locations, the rest of the code is under the Redstone XML-RPC path of the source code repository. The rest of the code should apply to both the Apache and Redstone XML-RPC server implementations. Only the server code, and JARs used + classpath for compilation are different between the two.

So you can check out the rest of the code from the Redstone path.

NOTE: however that XML-RPC structs are Java HashMaps and XML-RPC arrays are ArrayLists per the Redstone library. If Apache library implements differently, then need to swap out the relevant code in RemoteServerMethods class.