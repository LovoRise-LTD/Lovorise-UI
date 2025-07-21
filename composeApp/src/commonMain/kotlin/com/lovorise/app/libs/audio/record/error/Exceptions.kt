package com.lovorise.app.libs.audio.record.error

class NoOutputFileException : Exception("No output file")
class RecordFailException : Exception("Could not record audio")
class PermissionMissingException : Exception("The required permission is missing")
