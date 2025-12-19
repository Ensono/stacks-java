import urllib.request
import xml.etree.ElementTree as ET

def get_versions(group_id, artifact_id):
    path = f"{group_id.replace('.', '/')}/{artifact_id}/maven-metadata.xml"
    url = f"https://repo1.maven.org/maven2/{path}"
    
    try:
        with urllib.request.urlopen(url) as response:
            content = response.read()
            
        root = ET.fromstring(content)
        versions = []
        for version_elem in root.findall(".//version"):
            versions.append(version_elem.text)
        
        # Maven metadata usually lists versions in order of release, so last is latest
        return list(reversed(versions))
    except Exception as e:
        print(f"Error fetching {group_id}:{artifact_id}: {e}")
        return []

def check_dependency(group_id, artifact_id, current_version, previous_version):
    print(f"\nChecking {group_id}:{artifact_id}...")
    versions = get_versions(group_id, artifact_id)
    
    if not versions:
        return

    latest = versions[0]
    print(f"Latest available version: {latest}")
    
    if current_version in versions:
        print(f"Current configured version {current_version} EXISTS.")
    else:
        print(f"Current configured version {current_version} does NOT exist.")

    if previous_version in versions:
        print(f"Previous configured version {previous_version} EXISTS.")
    else:
        print(f"Previous configured version {previous_version} does NOT exist.")
        
    print(f"Top 5 versions: {versions[:5]}")

if __name__ == "__main__":
    # Serenity: 4.3.4 (current) vs 4.2.26 (previous)
    check_dependency("net.serenity-bdd", "serenity-core", "4.3.4", "4.2.26")
    
    # Cucumber: 7.33.0 (current) vs 7.22.2 (previous)
    check_dependency("io.cucumber", "cucumber-java", "7.33.0", "7.22.2")
