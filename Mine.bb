Type MINE
	Field Entity
End Type

Const MINE_FILE$="Mine"

Global MINE_MASTER

Function MineAnimFile$()
	Return AnimFile(MINE_FILE)
End Function

Function MineMatFile$()
	Return MatFile(MINE_FILE)
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D