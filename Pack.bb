Include"Offsets.bb" 

Global OFFSET=0
Const PACKFILE$="Assets.pak"

Function UnPackAsset$(PackedOffset,PackedLength)
	Local File=OpenFile(VisualDir()+PACKFILE)
	Local Bank=CreateBank(PackedLength)
	SeekFile(File,PackedOffset)
	ReadBytes(Bank,File,0,PackedLength)
	CloseFile File
	
	Local FileName$=ReadPackedFileName(Bank)
	
	File=WriteFile(TempDir()+FileName)
	
	WriteBytes Bank,File,32,PackedLength-32
	
	CloseFile File
	
	Return  TempDir()+FileName$
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

Function ReadPackedFileName$(Bank)
	Local Off
	Local Word$=""
	For Off=4 To 31
		Word=Word+Chr(PeekByte(Bank,Off))
	Next
	Return Trim(Word)
End Function
;~IDEal Editor Parameters:
;~F#5#17#2A
;~C#Blitz3D