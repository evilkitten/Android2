Global OFFSET=0
Const PACKFILE$="Package.pak"

;FOR EXTRACTION OF PACKAGED ASSETS
;UNCOMMENT WHEN ASSETS COMPLETE AND PACKED - ALSO REMEMBER TO CHANGE "MatfIle()" and "AnimFile()" IN "Init.bb"
Function UnPackAssets()
;	Local File=ReadFile(VisualDir()+PACKFILE)
	
;	While Not (Eof(File))
;		Local Length=ReadInt(File)
;		SeekFile(File,OFFSET+4)
;		Local FN$=ReadPackedFileName$(File)
;		SeekFile(File,OFFSET+32)
;		Local Bank=CreateBank(Length)
;		ReadBytes(Bank,File,0,Length)
;		Local OutPut=WriteFile(TempDir()+FN)
;		WriteBytes(Bank,OutPut,0,Length)
;		CloseFile OutPut
;		OFFSET=OFFSET+Length+32
;		SeekFile(File,OFFSET)
;	Wend
End Function

Function RemoveUnPackedAssets()
	Local DIR=ReadDir(TempDir())
	Local FN$=NextFile(DIR)
	Local FP$
	Local Ext$
	
	While (FN<>"")
		If (Len(FN)>4)
			Ext=Lower(Right(FN,4))
			If ((Ext=MESH_EXTENSION) Or (Ext=IMG_EXTENSION))
				FP$=TempDir()+FN$	
				DeleteFile FP
			End If
		End If	
		FN=NextFile(DIR)
	Wend
	CloseDir DIR
End Function

Function ReadPackedFileName$(File)
	Local Word$=""
	While (Len(Word)<=27)
		Word=Word+Chr(ReadByte(File))
	Wend
	Return Trim(Word)
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D