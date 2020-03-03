require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name     = "react-native-sqlcipher"
  s.version  = package['version']
  s.summary  = package['description']
  s.homepage = package['homepage']
  s.license  = package['license']
  s.author   = package['author']
  s.source   = { :git => 'https://github.com/linchenhuicn/react-native-sqlcipher.git', :tag => "v#{s.version}" }

  s.ios.deployment_target = '8.0'
  s.osx.deployment_target = '10.10'

  s.preserve_paths = 'README.md', 'LICENSE', 'package.json', 'sqlite.js'
  s.source_files   = "platforms/ios/*.{h,m}"

  s.dependency 'React'
#  s.library = 'sqlite3'

  s.dependency 'SQLCipher'
  s.compiler_flags = ['-DSQLCIPHER=1']
end
